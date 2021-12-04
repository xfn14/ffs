# Mensagem

Informação separada por @ ou ;

## Prefixo

FT-Rapid-Protocol@<versao>

## Type

- FilePacket
- FolderStatus
- etc

## Data

Conteudo do packet dependendo do tipo

### FilePacket

#### ID

Numero do packet a ser enviado pela ordem que o ficheiro foi divido

#### File Data

Conteudo do ficheiro em bytes.

### Folder Status

#### Length

Numero de ficheiros na pasta

#### Ficheiros

(Nome,tamanho)