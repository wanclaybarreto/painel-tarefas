<h1>PAINEL DE TAREFAS</h1>

Gestor de tarefas com design responsivo criado para administrar os afazeres relacionadas ao desenvolvimento de softwares em empresas do ramo de TI.
Tecnologias utilizadas: Java; Spring Data JPA, Spring Security e vários outros recursos do ecossistema Spring; banco de dados MySQL; Jaspersoft Studio;
Thymeleaf; HTML; CSS; Javascript.

É importante salientar que o foco deste projeto foi mais direcionado para a camada Back-End. Dessa forma, a parte visual poderá ser melhor trabalhada nas
próximas versões.

<h3>Usuários:</h3>

	A aplicação conta com três tipos de usuário: Administrador, Desenvolvedor e Técnico.
	
	Administrador: possui total acesso a todos os recursos do sistema.
	Desenvolvedor: possui acesso limitado; não tem permissão para consultar, cadastrar, alterar ou deletar Clientes, Administradores e Desenvolvedores.
	Técnico: possui acesso limitado; não tem permissão para consultar, cadastrar, alterar ou deletar Situações, Módulos, Clientes, Administradores, Desenvolvedores e Técnicos.
	
	É importante salientar que um usuário do tipo Administrador, o administrador padrão, é criado de forma automática pelo sistema.
	Esse usuário não pode ser alterado ou deletado. Para acessar o sistema com esse administrador padrão utilize os seguintes dados:
	
	-> e-mail: admin@email.com
	-> senha: admin123

<h3>Tarefas:</h3>

	As Tarefas possuem vários atributos, inclusive os que se relacionam com as Situações, Módulos, Clientes, Administradores, Desenvolvedores e Técnicos.

<h3>Situações:</h3>

	Se referem ao estado de uma tarefa.

<h3>Módulos:</h3>

	Servem para representar as diferentes camadas/eixos de um sistema.

<h3>Vale ressaltar que Situações, Módulos, Clientes, Administradores, Desenvolvedores e Técnicos só podem ser deletados se não estiverem sendo referenciados por nenhuma tarefa.</h3>

<h3>Relatórios:</h3>

	Essa aplicação conta com o recurso de geração de relatórios de tarefas em PDF. Esses relatórios podem ser criados com a aplicação dos seguintes
	filtros: situação, cliente, módulo, técnico, desenvolvedor e data.

	É importante salientar que os relatórios foram desenvolvidos com o auxílio do Jaspersoft Studio.


<h3>Melhorias para a próxima versão:</h3>

<ul>
	<li>Criar tela de alteração de senha para técnicos, desenvolvedores e administradores;</li>
	<li>Centralizar códigos (HTML, JS e CSS) redundantes relacionados ao modal de deleção de registros;</li>
	<li>Implementar filtro por datas realizado através de queries, já que os mesmos estão sendo realizados através de algoritmos com expressões lambda.</li>
</ul>
<br/>

Autor: Wanclay Barreto <br/>
Github: https://github.com/wanclaybarreto