Para correr e testar a aplicação,  ́e necessário executar os ficheiros ucDrive.jar e terminal.jar. 
O ucDrive.jar inicializa um servidor como primário. Se for inicializado outro, será considerado secundário. 
O terminal.jar inicializa uma instância de cliente que se conecta ao servidor primário.

Como o projeto foi realizado num grupo de dois alunos, não há um consola de administração,
mas sim ficheiros com a configuração estática do servidor e do cliente. 

Usar os seguintes comandos para correr:
	$ java -jar ucDrive.jar
	$ java -jar terminal.jar

Para o funcionamento de ambas as aplicações,  ́e necessário um ficheiro denominado "config.yaml" na pasta
server, para os servidores, e client para os v ́arios clientes. Ambas as pastas server e client precisam
de ter uma pasta users, com os v ́arios utilizadores das aplica ̧c ̃oes. A pasta entregue cont ́em j ́a as
pastas e os ficheiros necess ́arios para o funcionamento das aplica ̧c ̃oes cliente e servidor.