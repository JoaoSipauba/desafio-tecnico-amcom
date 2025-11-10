# Integração — Serviço A — Eventos de Pedido (v1)

## Visão geral
Evento emitido via Kafka pelo Serviço A (somente eventos de criação `CREATE`). `externalId` é a chave de idempotência e particionamento para garantir ordem por pedido.

## Transporte
- Broker: Kafka  
- Tópico: `order-events`  
- Key (partition key): `externalId`  

## Contrato do payload
Campos:
- `externalId` (string, UUID) — obrigatório  
- `eventType` (string, enum) — sempre `CREATE`  
- `items` (array) — obrigatório em todos os eventos (somente criação)  
- `productId` (string, non-empty)  
- `quantity` (integer, >= 0)  
- `unitPrice` (number, > 0, 2 casas decimais)

## Exemplos

### CREATE
```json
{
  "externalId": "98aa2568-eb70-4405-bb9f-ed11c7285152",
  "eventType": "CREATE",
  "items": [
    { "productId": "A1", "quantity": 2, "unitPrice": 10.50 },
    { "productId": "B2", "quantity": 1, "unitPrice": 20.00 }
  ]
}
```

## Regras de validação
- `externalId`: obrigatório, string não vazia (UUID recomendado)
- `eventType`: obrigatório e deve ser `CREATE`
- `items`: obrigatório e não vazio
- Cada item: `productId` não vazio, `quantity` >= 0 (aceita 0), `unitPrice` > 0

## Garantias e ordenação
- Entrega: at-least-once (podem ocorrer duplicidades)
- Ordenação: garantida por `externalId` quando usado como chave de partição
- Idempotência: o consumidor trata duplicidades por `externalId`

## Retry e DLQ
- Política de retentativas: backoff fixo ~1s, até 5 tentativas
- Mensagens que excedem as tentativas ou inválidas seguem para `order-events.DLQ`

## JSON Schema (v1)
```json
{
  "externalId": "98aa2568-eb70-4405-bb9f-ed11c7285152",
  "items": [
    {
      "productId": "A1",
      "quantity": 2,
      "unitPrice": 10.50
    },
    {
      "productId": "B2",
      "quantity": 1,
      "unitPrice": 20.00
    }
  ],
  "eventType": "CREATE"
}
```