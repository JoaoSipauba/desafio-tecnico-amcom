# Integração — Serviço B — Processamento de Pedidos (v1)

## Visão Geral
O Serviço B consome eventos de pedidos processados de criação enviados pelo Serviço principal (somente com `externalId`) e enriquece o contexto consultando o endpoint HTTP `/v1/orders/{externalId}`. Após avaliação de regras próprias (ex.: verificação de estoque, antifraude, SLA), publica novos eventos de resultado (`COMPLETE` ou `CANCEL`) no tópico `orders-event`.

## Fluxo Resumido
1. Consome mensagem do tópico `order-processed` contendo apenas `externalId` (evento de criação previamente persistido).
2. Realiza requisição HTTP `GET /v1/orders/{externalId}` para obter dados completos do pedido.
3. Executa lógica de decisão (exemplos: validação de pagamento, limite de crédito, disponibilidade de item).
4. Publica evento de resultado (tipo `COMPLETE` ou `CANCEL`) no tópico `orders-event`.
5. Consumidores downstream interpretam o novo estado do pedido a partir de `orders-event`.

## Tópicos Kafka
| Direção | Tópico | Descrição |
|---------|--------|-----------|
| IN      | `order-processed` | Evento inicial pós-criação (payload mínimo). |
| OUT     | `orders-event` | Evento de atualização de estado (RESULT: COMPLETE/CANCEL). |

Observação: Diferentes tópicos são usados para separar o fluxo de criação (`order-processed`) do fluxo de conclusão/cancelamento (`orders-event`).

## Contratos de Mensagem
### Entrada (CreateProcessedEventV1)
Payload mínimo (somente referência):
```json
{
  "externalId": "d7c1e0fd-6c3d-4584-a2bf-1f8d5c3e9a11"
}
```

### Saída (OrderResultEventV1)
Eventos de conclusão ou cancelamento:
Campos:
- `externalId` (string, UUID) — obrigatório
- `eventType` (string, enum) — `COMPLETE` | `CANCEL` (obrigatório)

#### Exemplo COMPLETE
```json
{
  "externalId": "d7c1e0fd-6c3d-4584-a2bf-1f8d5c3e9a11",
  "eventType": "COMPLETE"
}
```

#### Exemplo CANCEL
```json
{
  "externalId": "d7c1e0fd-6c3d-4584-a2bf-1f8d5c3e9a11",
  "eventType": "CANCEL"
}
```

## Regras de Validação (Saída)
- `externalId`: não vazio
- `eventType`: `COMPLETE` ou `CANCEL`

## Chamadas HTTP para Enriquecimento
- Método: `GET`
- Endpoint: `/v1/orders/{externalId}`

## Estratégia de Idempotência
Eventos de saída em `orders-event` são idempotentes por `externalId` + `eventType`.

## DLQ & Retry
- Falhas de serialização ou validação (saída) → DLQ (`orders-event.DLQ`)
- Falhas transitórias HTTP → retentativas internas antes de possível CANCEL por timeout
