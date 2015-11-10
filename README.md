Projeto adaptado do projeto original https://github.com/decarli/softlayer-object-storage-java/tree/master/sl-objectstorage

-----

Para testar adicionar as credenciais no arquivos FileUploadEndpoint.

baseUrl ==> Authentication Endpoint
Colocar somente: https://dal05.objectstorage.softlayer.net
Não colocar nada depois do .net

user: Username

password: API Key (Password)

location: Local de armazenamento, nome do container. Se tiver pastas colocar / e o nome dela.


-----

Projeto visa receber um arquivos através de um serviço Restful de uma página html e envia-lo para o ObjectStore da softlayer.

Desenvolvido com JEE7.

Testado com Wildfly 9.0.1

Para testar:

http://localhost:8080/sl

Fazer o upload do arquivo. Ok

Se as credenciais estiverem corretas o mesmo irá para a Softlayer ObjectStore.

[]'s

