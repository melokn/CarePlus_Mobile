Para usar o aplicativo mobile CarePlus:

Você vai precisar instalar:

- Node.JS
- Docker / Docker Desktop

clone o seguinte repositorio para ter acesso ao backend:
https://github.com/melokn/CarePlus.git

então, no terminal do backend, insira os seguintes comandos:

- npx prisma generate
- docker compose up
- npm run dev

deixe o terminal aberto, rodando a api.

opcional:
 - em caso de erro, use npx prisma migrate -dev
 - para visualizar o banco de dados, use npx prisma studio

então, use o APP do CarePlus. :D
