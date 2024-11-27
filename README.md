# nimbus - server

Este repositório versiona o back-end do sistema **Nimbus**, tendo como objetivo a permanência e comunicação de dados para gerenciamento de atletas e times de basquete.

O sistema é separado em três **microsserviços**: 
- Usuários (users_ms)
- Eventos (events_ms)
- Chat (chat_ms)

## Users microservice

Na **nimbus**, os usuários podem ser registrados como atletas ou treinadores, possuindo ambos registros como **User** e seu relacionamento com seu determinado tipo de usuário.
### UserController

#### Registrar usuário
```http
POST /users
```

#### Realiza login
```http
POST /users/login
```

#### Requisição de mudança de senha
```http
POST /users/change-password-request
```

#### Busca usuário por id
```http
GET /users/{id}
```

#### Alterar senha
```http
PATCH /users/change-password/{id}
```

#### Deletar registro de usuário
```http
DELETE /users/{id}
```

### AthleteController

#### Alterar informações de atleta
```http
PUT /athletes
```

#### Registrar atleta em time
```http
PATCH /athletes/register-team/{id}
```

### AthleteDescController

#### Registrar descrições de atleta
```http
POST /athlete-descs
```

#### Busca descrições de atleta
```http
GET /athlete-descs/{athleteId}
```

#### Atualiza descrições de atleta
```http
PUT /athlete-descs/{athleteId}
```

### AthleteHistoricController

#### Registra histórico de atleta
```http
POST /athlete-historics
```

#### Busca históricos de atleta por id do atleta
```http
GET /athlete-historics/from-athlete/{athleteId}
```

#### Busca históricos de atleta por id do atleta pageada
```http
GET /athlete-historics/page-from-athlete/{athleteId}
```
Params: page & elements

#### Deleta histórico de atleta
```http
DELETE /athlete-historics/{id}
```

### CoachController

#### Alterar informações de terinador
```http
PUT /coaches
```

### InjuryController

#### Registrar lesão de atleta
```http
POST /injuries
```

#### Buscar lesões de atleta
```http
GET /injuries/from-athlete/{athleteId}
```

#### Atualiza registro de lesão
```http
PUT /injuries/{id}
```

#### Deleta registro de lesão
```http
DELETE /injuries/{id}
```

### TeamController

#### Registra novo time
```http
POST /teams
```

#### Busca um time
```http
GET /teams/{id}
```

#### Busca lesões ativas de um time
```http
GET /teams/active-injuries/{id}
```

#### Atualiza informações do time
```http
PUT /teams/{id}
```

#### Solicitar mudança de treinador do time
```http
PATCH /teams/change-owner-request/{id}
```

#### Valida código para mudança de treinador do time
```http
PATCH /teams/change-team-owner-by-code/{id}
```

#### Valida código para mudança de treinador do time
```http
DELETE /teams/{id}
```

### BlobStorageController

#### Registra arquivo em Azure Blob Storage
```http
POST /blob-storage
```

### OperationCodeController

#### Valida código de operação
```http
GET /codes/validate-code
```
