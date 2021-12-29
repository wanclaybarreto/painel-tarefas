//Remoção do evento de click oriundo da tag de link no logotipo da ASN:
document.querySelector("nav ul li:nth-child(1) a").addEventListener("click", (e) => {
    e.preventDefault();
});



//Menu toggle:
document.querySelector(".toggle").addEventListener("click", toggleMenu);

function toggleMenu() {
    document.querySelector(".toggle").classList.toggle("active");
    document.querySelector("nav").classList.toggle("active");
    document.querySelector(".main").classList.toggle("active");
}



//Preview da imagem e do vídeo selecionados
if (document.querySelector("input[name='imagemFile']") && document.querySelector("input[name='videoFile']")) {
    document.querySelector("input[name='imagemFile']").addEventListener("change", (ev) => {
    
        let file = ev.target.files.item(0);
    
        let reader = new FileReader();
    
        reader.onload = e => {
			document.querySelector(".imgSelected img").src = e.target.result;
			document.querySelector(".tarefaBox .modalEnlargedImgSelected img").src = e.target.result;
		}
    
        reader.readAsDataURL(file);
    
    });
    
    document.querySelector("input[name='videoFile']").addEventListener("change", (ev) => {
        
        let file = ev.target.files.item(0);
    
        let reader = new FileReader();
    
        reader.onload = e => document.querySelector(".vidSelected video").src = e.target.result;
    
        reader.readAsDataURL(file);
    
    });

	document.querySelector(".tarefaBox .group .imgSelected .zoomImgSelected img").addEventListener("click", () => {
		document.querySelector(".tarefaBox .modalEnlargedImgSelected").style.display = 'flex';
	});
	
	document.querySelector(".tarefaBox .modalEnlargedImgSelected .closer").addEventListener("click", () => {
		document.querySelector(".tarefaBox .modalEnlargedImgSelected").style.display = 'none';
	});
}



//Botões para editar/detalhar as tarefas
var frmSendIdTarefaToEdit = document.querySelector("form[name='frmSendIdTarefaToEdit']");

if (frmSendIdTarefaToEdit) {
	let btnsEditTarefa = Array.from(document.querySelectorAll(".tarefa .toEditTarefa"));
	
	btnsEditTarefa.forEach((btn) => {
		btn.addEventListener("click", () => {
			frmSendIdTarefaToEdit.querySelector("input[name='tarefa-id']").value = btn.closest(".tarefa").getAttribute("data-tarefa-id");
			frmSendIdTarefaToEdit.submit();
		});
	});
}



//Impedidindo que os caracteres "," e "." sejam inseridos
function isNumberInteger(evt) {
	var charCode = (evt.which) ? evt.which : evt.keyCode;
	
	if (charCode == 110 || charCode == 188 || charCode == 190 || charCode == 194) {
		return false;
	}
	
	return true;
}



//Personalizando coluna de cor da tabela de Situações; implementando funcionalidade de preenchimento de dados
//para edição a partir de cliques na tabela; clique para deletar situação
var tabelaSituacoes = document.querySelector(".situacoesBox .situacoesConsAlter table");
var imgSituacaoDeleteClicked = false;
var confirmDeleteSituacaoModal = document.querySelector(".confirmDeleteSituacaoModal");

if (tabelaSituacoes) {
    for (let i = 1; i < tabelaSituacoes.rows.length; i++) {

        tabelaSituacoes.rows[i].cells[3].style.color = tabelaSituacoes.rows[i].cells[3].innerHTML;
        
        tabelaSituacoes.rows[i].addEventListener("click", (event) => {            
        	document.querySelector(".situacoesBox .situacoesConsAlter form[name='situacaoEdForm'] input[name='id']").value = event.target.closest("tr").cells[0].innerHTML;
        	document.querySelector(".situacoesBox .situacoesConsAlter form[name='situacaoEdForm'] input[name='nome']").value = event.target.closest("tr").cells[1].innerHTML;
        	document.querySelector(".situacoesBox .situacoesConsAlter form[name='situacaoEdForm'] input[name='indice']").value = event.target.closest("tr").cells[2].innerHTML;
        	document.querySelector(".situacoesBox .situacoesConsAlter form[name='situacaoEdForm'] input[name='cor']").value = event.target.closest("tr").cells[3].innerHTML;

            enableSituacaoFieldsAlter();

			if (imgSituacaoDeleteClicked) {
				disableSituacaoFieldsAlter();
				imgSituacaoDeleteClicked = false;
			}
        });

    }

	tabelaSituacoes.querySelectorAll("img").forEach((imgDelete) => {
		
		imgDelete.addEventListener("click", (event) => {
			confirmDeleteSituacaoModal.querySelector(".text-modal").innerHTML = confirmDeleteSituacaoModal.querySelector(".text-modal").innerHTML.replace("#sit#", event.target.closest("tr").cells[1].innerHTML);
			confirmDeleteSituacaoModal.querySelector(".idSituacao").innerHTML = event.target.closest("tr").cells[0].innerHTML;
			confirmDeleteSituacaoModal.querySelector(".nomeSituacao").innerHTML = event.target.closest("tr").cells[1].innerHTML;
			
			confirmDeleteSituacaoModal.style.display = 'flex';
			
			imgSituacaoDeleteClicked = true;
		});
		
	});

    document.querySelector(".situacoesBox .situacoesConsAlter input[type='reset']").addEventListener("click", () => {
        disableSituacaoFieldsAlter();
    });

	confirmDeleteSituacaoModal.querySelector(".cancelbtn").addEventListener("click", () => {
		confirmDeleteSituacaoModal.style.display = "none";
		confirmDeleteSituacaoModal.querySelector(".text-modal").innerHTML = "Deseja realmente deletar a situação \"#sit#\"?";
	});
	
	confirmDeleteSituacaoModal.querySelector(".confirmbtn").addEventListener("click", () => {
		document.querySelector("form[name='situacaoDelForm'] input[name='id-situacao-del']").value = confirmDeleteSituacaoModal.querySelector(".idSituacao").innerHTML;
		document.querySelector("form[name='situacaoDelForm']").submit();
	});
	
	document.querySelector("form[name='situacaoEdForm'] input[type='submit']").addEventListener("click", () => {
		
		document.querySelector("form[name='situacaoEdForm'] input[name='id']").disabled = false;
		document.querySelector("form[name='situacaoEdForm']").submit();
		
	});
}

function enableSituacaoFieldsAlter() {
    document.querySelectorAll(".situacoesBox .situacoesConsAlter input").forEach((input, index) => {
        if (index != 0) input.disabled = false;
    });

    document.querySelector(".situacoesBox .situacoesConsAlter input[type='submit']").classList.add("btnForm");
    document.querySelector(".situacoesBox .situacoesConsAlter input[type='reset']").classList.add("btnForm");
    
}

function disableSituacaoFieldsAlter() {
    document.querySelectorAll(".situacoesBox .situacoesConsAlter input").forEach((input) => {
        input.disabled = true;
        if (input.type != 'submit' && input.type != 'reset') input.value = '';
    });

    document.querySelector(".situacoesBox .situacoesConsAlter input[type='submit']").classList.remove("btnForm");
    document.querySelector(".situacoesBox .situacoesConsAlter input[type='reset']").classList.remove("btnForm");
}



//Implementando funcionalidade de preenchimento de dados
//para edição a partir de cliques na tabela; clique para deletar módulo
var tabelaModulos = document.querySelector(".modulosBox .modulosConsAlter table");
var imgModuloDeleteClicked = false;
var confirmDeleteModuloModal = document.querySelector(".confirmDeleteModuloModal");

if (tabelaModulos) {
    for (let i = 1; i < tabelaModulos.rows.length; i++) {
        
        tabelaModulos.rows[i].addEventListener("click", (event) => {            
        	document.querySelector(".modulosBox .modulosConsAlter form[name='moduloEdForm'] input[name='id']").value = event.target.closest("tr").cells[0].innerHTML;
        	document.querySelector(".modulosBox .modulosConsAlter form[name='moduloEdForm'] input[name='nome']").value = event.target.closest("tr").cells[1].innerHTML;

            enableModuloFieldsAlter();

			if (imgModuloDeleteClicked) {
				disableModuloFieldsAlter();
				imgModuloDeleteClicked = false;
			}
        });

    }

	tabelaModulos.querySelectorAll("img").forEach((imgDelete) => {
		
		imgDelete.addEventListener("click", (event) => {
			confirmDeleteModuloModal.querySelector(".text-modal").innerHTML = confirmDeleteModuloModal.querySelector(".text-modal").innerHTML.replace("#mod#", event.target.closest("tr").cells[1].innerHTML);
			confirmDeleteModuloModal.querySelector(".idModulo").innerHTML = event.target.closest("tr").cells[0].innerHTML;
			confirmDeleteModuloModal.querySelector(".nomeModulo").innerHTML = event.target.closest("tr").cells[1].innerHTML;
			
			confirmDeleteModuloModal.style.display = 'flex';
			
			imgModuloDeleteClicked = true;
		});
		
	});

    document.querySelector(".modulosBox .modulosConsAlter input[type='reset']").addEventListener("click", () => {
        disableModuloFieldsAlter();
    });

	confirmDeleteModuloModal.querySelector(".cancelbtn").addEventListener("click", () => {
		confirmDeleteModuloModal.style.display = "none";
		confirmDeleteModuloModal.querySelector(".text-modal").innerHTML = "Deseja realmente deletar o módulo \"#mod#\"?";
	});
	
	confirmDeleteModuloModal.querySelector(".confirmbtn").addEventListener("click", () => {
		document.querySelector("form[name='moduloDelForm'] input[name='id-modulo-del']").value = confirmDeleteModuloModal.querySelector(".idModulo").innerHTML;
		document.querySelector("form[name='moduloDelForm']").submit();
	});
	
	document.querySelector("form[name='moduloEdForm'] input[type='submit']").addEventListener("click", () => {
		
		document.querySelector("form[name='moduloEdForm'] input[name='id']").disabled = false;
		document.querySelector("form[name='moduloEdForm']").submit();
		
	});
}

function enableModuloFieldsAlter() {
    document.querySelectorAll(".modulosBox .modulosConsAlter input").forEach((input, index) => {
        if (index != 0) input.disabled = false;
    });

    document.querySelector(".modulosBox .modulosConsAlter input[type='submit']").classList.add("btnForm");
    document.querySelector(".modulosBox .modulosConsAlter input[type='reset']").classList.add("btnForm");
    
}

function disableModuloFieldsAlter() {
    document.querySelectorAll(".modulosBox .modulosConsAlter input").forEach((input) => {
        input.disabled = true;
        if (input.type != 'submit' && input.type != 'reset') input.value = '';
    });

    document.querySelector(".modulosBox .modulosConsAlter input[type='submit']").classList.remove("btnForm");
    document.querySelector(".modulosBox .modulosConsAlter input[type='reset']").classList.remove("btnForm");
}



//Implementando funcionalidade de preenchimento de dados
//para edição a partir de cliques na tabela; clique para deletar cliente
var tabelaClientes = document.querySelector(".clientesBox .clientesConsAlter table");
var imgClienteDeleteClicked = false;
var confirmDeleteClienteModal = document.querySelector(".confirmDeleteClienteModal");

if (tabelaClientes) {
    for (let i = 1; i < tabelaClientes.rows.length; i++) {
        
        tabelaClientes.rows[i].addEventListener("click", (event) => {            
        	document.querySelector(".clientesBox .clientesConsAlter form[name='clienteEdForm'] input[name='id']").value = event.target.closest("tr").cells[0].innerHTML;
        	document.querySelector(".clientesBox .clientesConsAlter form[name='clienteEdForm'] input[name='nome']").value = event.target.closest("tr").cells[1].innerHTML;

            enableClienteFieldsAlter();

			if (imgClienteDeleteClicked) {
				disableClienteFieldsAlter();
				imgClienteDeleteClicked = false;
			}
        });

    }

	tabelaClientes.querySelectorAll("img").forEach((imgDelete) => {
		
		imgDelete.addEventListener("click", (event) => {
			confirmDeleteClienteModal.querySelector(".text-modal").innerHTML = confirmDeleteClienteModal.querySelector(".text-modal").innerHTML.replace("#cli#", event.target.closest("tr").cells[1].innerHTML);
			confirmDeleteClienteModal.querySelector(".idCliente").innerHTML = event.target.closest("tr").cells[0].innerHTML;
			confirmDeleteClienteModal.querySelector(".nomeCliente").innerHTML = event.target.closest("tr").cells[1].innerHTML;
			
			confirmDeleteClienteModal.style.display = 'flex';
			
			imgClienteDeleteClicked = true;
		});
		
	});

    document.querySelector(".clientesBox .clientesConsAlter input[type='reset']").addEventListener("click", () => {
        disableClienteFieldsAlter();
    });

	confirmDeleteClienteModal.querySelector(".cancelbtn").addEventListener("click", () => {
		confirmDeleteClienteModal.style.display = "none";
		confirmDeleteClienteModal.querySelector(".text-modal").innerHTML = "Deseja realmente deletar o cliente \"#cli#\"?";
	});
	
	confirmDeleteClienteModal.querySelector(".confirmbtn").addEventListener("click", () => {
		document.querySelector("form[name='clienteDelForm'] input[name='id-cliente-del']").value = confirmDeleteClienteModal.querySelector(".idCliente").innerHTML;
		document.querySelector("form[name='clienteDelForm']").submit();
	});
	
	document.querySelector("form[name='clienteEdForm'] input[type='submit']").addEventListener("click", () => {
		
		document.querySelector("form[name='clienteEdForm'] input[name='id']").disabled = false;
		document.querySelector("form[name='clienteEdForm']").submit();
		
	});
}

function enableClienteFieldsAlter() {
    document.querySelectorAll(".clientesBox .clientesConsAlter input").forEach((input, index) => {
        if (index != 0) input.disabled = false;
    });

    document.querySelector(".clientesBox .clientesConsAlter input[type='submit']").classList.add("btnForm");
    document.querySelector(".clientesBox .clientesConsAlter input[type='reset']").classList.add("btnForm");
    
}

function disableClienteFieldsAlter() {
    document.querySelectorAll(".clientesBox .clientesConsAlter input").forEach((input) => {
        input.disabled = true;
        if (input.type != 'submit' && input.type != 'reset') input.value = '';
    });

    document.querySelector(".clientesBox .clientesConsAlter input[type='submit']").classList.remove("btnForm");
    document.querySelector(".clientesBox .clientesConsAlter input[type='reset']").classList.remove("btnForm");
}



//Implementando funcionalidade de preenchimento de dados
//para edição a partir de cliques na tabela; clique para deletar técnico
var tabelaTecnicos = document.querySelector(".tecnicosBox .tecnicosConsAlter table");
var imgTecnicoDeleteClicked = false;
var confirmDeleteTecnicoModal = document.querySelector(".confirmDeleteTecnicoModal");

if (tabelaTecnicos) {
    for (let i = 1; i < tabelaTecnicos.rows.length; i++) {
        
        tabelaTecnicos.rows[i].addEventListener("click", (event) => {            
        	document.querySelector(".tecnicosBox .tecnicosConsAlter form[name='tecnicoEdForm'] input[name='id']").value = event.target.closest("tr").cells[0].innerHTML;
        	document.querySelector(".tecnicosBox .tecnicosConsAlter form[name='tecnicoEdForm'] input[name='nome']").value = event.target.closest("tr").cells[1].innerHTML;
			document.querySelector(".tecnicosBox .tecnicosConsAlter form[name='tecnicoEdForm'] input[name='email']").value = event.target.closest("tr").cells[2].innerHTML;

            enableTecnicoFieldsAlter();

			if (imgTecnicoDeleteClicked) {
				disableTecnicoFieldsAlter();
				imgTecnicoDeleteClicked = false;
			}
        });

    }

	tabelaTecnicos.querySelectorAll("img").forEach((imgDelete) => {
		
		imgDelete.addEventListener("click", (event) => {
			confirmDeleteTecnicoModal.querySelector(".text-modal").innerHTML = confirmDeleteTecnicoModal.querySelector(".text-modal").innerHTML.replace("#tec#", event.target.closest("tr").cells[1].innerHTML);
			confirmDeleteTecnicoModal.querySelector(".idTecnico").innerHTML = event.target.closest("tr").cells[0].innerHTML;
			confirmDeleteTecnicoModal.querySelector(".nomeTecnico").innerHTML = event.target.closest("tr").cells[1].innerHTML;
			
			confirmDeleteTecnicoModal.style.display = 'flex';
			
			imgTecnicoDeleteClicked = true;
		});
		
	});

    document.querySelector(".tecnicosBox .tecnicosConsAlter input[type='reset']").addEventListener("click", () => {
        disableTecnicoFieldsAlter();
    });

	confirmDeleteTecnicoModal.querySelector(".cancelbtn").addEventListener("click", () => {
		confirmDeleteTecnicoModal.style.display = "none";
		confirmDeleteTecnicoModal.querySelector(".text-modal").innerHTML = "Deseja realmente deletar o técnico \"#tec#\"?";
	});
	
	confirmDeleteTecnicoModal.querySelector(".confirmbtn").addEventListener("click", () => {
		document.querySelector("form[name='tecnicoDelForm'] input[name='id-tecnico-del']").value = confirmDeleteTecnicoModal.querySelector(".idTecnico").innerHTML;
		document.querySelector("form[name='tecnicoDelForm']").submit();
	});
	
	document.querySelector("form[name='tecnicoEdForm'] input[type='submit']").addEventListener("click", () => {
		
		document.querySelector("form[name='tecnicoEdForm'] input[name='id']").disabled = false;
		document.querySelector("form[name='tecnicoEdForm'] input[name='senha']").value = "******";
		document.querySelector("form[name='tecnicoEdForm']").submit();
		
	});
}

function enableTecnicoFieldsAlter() {
    document.querySelectorAll(".tecnicosBox .tecnicosConsAlter input").forEach((input, index) => {
        if (index != 0) input.disabled = false;
    });

    document.querySelector(".tecnicosBox .tecnicosConsAlter input[type='submit']").classList.add("btnForm");
    document.querySelector(".tecnicosBox .tecnicosConsAlter input[type='reset']").classList.add("btnForm");
    
}

function disableTecnicoFieldsAlter() {
    document.querySelectorAll(".tecnicosBox .tecnicosConsAlter input").forEach((input) => {
        input.disabled = true;
        if (input.type != 'submit' && input.type != 'reset') input.value = '';
    });

    document.querySelector(".tecnicosBox .tecnicosConsAlter input[type='submit']").classList.remove("btnForm");
    document.querySelector(".tecnicosBox .tecnicosConsAlter input[type='reset']").classList.remove("btnForm");
}



//Implementando funcionalidade de preenchimento de dados
//para edição a partir de cliques na tabela; clique para deletar desenvolvedor
var tabelaDesenvolvedores = document.querySelector(".desenvolvedoresBox .desenvolvedoresConsAlter table");
var imgDesenvolvedorDeleteClicked = false;
var confirmDeleteDesenvolvedorModal = document.querySelector(".confirmDeleteDesenvolvedorModal");

if (tabelaDesenvolvedores) {
    for (let i = 1; i < tabelaDesenvolvedores.rows.length; i++) {
        
        tabelaDesenvolvedores.rows[i].addEventListener("click", (event) => {            
        	document.querySelector(".desenvolvedoresBox .desenvolvedoresConsAlter form[name='desenvolvedorEdForm'] input[name='id']").value = event.target.closest("tr").cells[0].innerHTML;
        	document.querySelector(".desenvolvedoresBox .desenvolvedoresConsAlter form[name='desenvolvedorEdForm'] input[name='nome']").value = event.target.closest("tr").cells[1].innerHTML;
			document.querySelector(".desenvolvedoresBox .desenvolvedoresConsAlter form[name='desenvolvedorEdForm'] input[name='email']").value = event.target.closest("tr").cells[2].innerHTML;

            enableDesenvolvedorFieldsAlter();

			if (imgDesenvolvedorDeleteClicked) {
				disableDesenvolvedorFieldsAlter();
				imgDesenvolvedorDeleteClicked = false;
			}
        });

    }

	tabelaDesenvolvedores.querySelectorAll("img").forEach((imgDelete) => {
		
		imgDelete.addEventListener("click", (event) => {
			confirmDeleteDesenvolvedorModal.querySelector(".text-modal").innerHTML = confirmDeleteDesenvolvedorModal.querySelector(".text-modal").innerHTML.replace("#des#", event.target.closest("tr").cells[1].innerHTML);
			confirmDeleteDesenvolvedorModal.querySelector(".idDesenvolvedor").innerHTML = event.target.closest("tr").cells[0].innerHTML;
			confirmDeleteDesenvolvedorModal.querySelector(".nomeDesenvolvedor").innerHTML = event.target.closest("tr").cells[1].innerHTML;
			
			confirmDeleteDesenvolvedorModal.style.display = 'flex';
			
			imgDesenvolvedorDeleteClicked = true;
		});
		
	});

    document.querySelector(".desenvolvedoresBox .desenvolvedoresConsAlter input[type='reset']").addEventListener("click", () => {
        disableDesenvolvedorFieldsAlter();
    });

	confirmDeleteDesenvolvedorModal.querySelector(".cancelbtn").addEventListener("click", () => {
		confirmDeleteDesenvolvedorModal.style.display = "none";
		confirmDeleteDesenvolvedorModal.querySelector(".text-modal").innerHTML = "Deseja realmente deletar o desenvolvedor \"#des#\"?";
	});
	
	confirmDeleteDesenvolvedorModal.querySelector(".confirmbtn").addEventListener("click", () => {
		document.querySelector("form[name='desenvolvedorDelForm'] input[name='id-desenvolvedor-del']").value = confirmDeleteDesenvolvedorModal.querySelector(".idDesenvolvedor").innerHTML;
		document.querySelector("form[name='desenvolvedorDelForm']").submit();
	});
	
	document.querySelector("form[name='desenvolvedorEdForm'] input[type='submit']").addEventListener("click", () => {
		
		document.querySelector("form[name='desenvolvedorEdForm'] input[name='id']").disabled = false;
		document.querySelector("form[name='desenvolvedorEdForm'] input[name='senha']").value = "******";
		document.querySelector("form[name='desenvolvedorEdForm']").submit();
		
	});
}

function enableDesenvolvedorFieldsAlter() {
    document.querySelectorAll(".desenvolvedoresBox .desenvolvedoresConsAlter input").forEach((input, index) => {
        if (index != 0) input.disabled = false;
    });

    document.querySelector(".desenvolvedoresBox .desenvolvedoresConsAlter input[type='submit']").classList.add("btnForm");
    document.querySelector(".desenvolvedoresBox .desenvolvedoresConsAlter input[type='reset']").classList.add("btnForm");
    
}

function disableDesenvolvedorFieldsAlter() {
    document.querySelectorAll(".desenvolvedoresBox .desenvolvedoresConsAlter input").forEach((input) => {
        input.disabled = true;
        if (input.type != 'submit' && input.type != 'reset') input.value = '';
    });

    document.querySelector(".desenvolvedoresBox .desenvolvedoresConsAlter input[type='submit']").classList.remove("btnForm");
    document.querySelector(".desenvolvedoresBox .desenvolvedoresConsAlter input[type='reset']").classList.remove("btnForm");
}



//Implementando funcionalidade de preenchimento de dados
//para edição a partir de cliques na tabela; clique para deletar administrador
var tabelaAdministradores = document.querySelector(".administradoresBox .administradoresConsAlter table");
var imgAdministradorDeleteClicked = false;
var confirmDeleteAdministradorModal = document.querySelector(".confirmDeleteAdministradorModal");

if (tabelaAdministradores) {
    for (let i = 1; i < tabelaAdministradores.rows.length; i++) {
        
        tabelaAdministradores.rows[i].addEventListener("click", (event) => {            
        	document.querySelector(".administradoresBox .administradoresConsAlter form[name='administradorEdForm'] input[name='id']").value = event.target.closest("tr").cells[0].innerHTML;
        	document.querySelector(".administradoresBox .administradoresConsAlter form[name='administradorEdForm'] input[name='nome']").value = event.target.closest("tr").cells[1].innerHTML;
			document.querySelector(".administradoresBox .administradoresConsAlter form[name='administradorEdForm'] input[name='email']").value = event.target.closest("tr").cells[2].innerHTML;

            enableAdministradorFieldsAlter();

			if (imgAdministradorDeleteClicked) {
				disableAdministradorFieldsAlter();
				imgAdministradorDeleteClicked = false;
			}
        });

    }

	tabelaAdministradores.querySelectorAll("img").forEach((imgDelete) => {
		
		imgDelete.addEventListener("click", (event) => {
			confirmDeleteAdministradorModal.querySelector(".text-modal").innerHTML = confirmDeleteAdministradorModal.querySelector(".text-modal").innerHTML.replace("#adm#", event.target.closest("tr").cells[1].innerHTML);
			confirmDeleteAdministradorModal.querySelector(".idAdministrador").innerHTML = event.target.closest("tr").cells[0].innerHTML;
			confirmDeleteAdministradorModal.querySelector(".nomeAdministrador").innerHTML = event.target.closest("tr").cells[1].innerHTML;
			
			confirmDeleteAdministradorModal.style.display = 'flex';
			
			imgAdministradorDeleteClicked = true;
		});
		
	});

    document.querySelector(".administradoresBox .administradoresConsAlter input[type='reset']").addEventListener("click", () => {
        disableAdministradorFieldsAlter();
    });

	confirmDeleteAdministradorModal.querySelector(".cancelbtn").addEventListener("click", () => {
		confirmDeleteAdministradorModal.style.display = "none";
		confirmDeleteAdministradorModal.querySelector(".text-modal").innerHTML = "Deseja realmente deletar o administrador \"#adm#\"?";
	});
	
	confirmDeleteAdministradorModal.querySelector(".confirmbtn").addEventListener("click", () => {
		document.querySelector("form[name='administradorDelForm'] input[name='id-administrador-del']").value = confirmDeleteAdministradorModal.querySelector(".idAdministrador").innerHTML;
		document.querySelector("form[name='administradorDelForm']").submit();
	});
	
	document.querySelector("form[name='administradorEdForm'] input[type='submit']").addEventListener("click", () => {
		
		document.querySelector("form[name='administradorEdForm'] input[name='id']").disabled = false;
		document.querySelector("form[name='administradorEdForm'] input[name='senha']").value = "******";
		document.querySelector("form[name='administradorEdForm']").submit();
		
	});
}

function enableAdministradorFieldsAlter() {
    document.querySelectorAll(".administradoresBox .administradoresConsAlter input").forEach((input, index) => {
        if (index != 0) input.disabled = false;
    });

    document.querySelector(".administradoresBox .administradoresConsAlter input[type='submit']").classList.add("btnForm");
    document.querySelector(".administradoresBox .administradoresConsAlter input[type='reset']").classList.add("btnForm");
    
}

function disableAdministradorFieldsAlter() {
    document.querySelectorAll(".administradoresBox .administradoresConsAlter input").forEach((input) => {
        input.disabled = true;
        if (input.type != 'submit' && input.type != 'reset') input.value = '';
    });

    document.querySelector(".administradoresBox .administradoresConsAlter input[type='submit']").classList.remove("btnForm");
    document.querySelector(".administradoresBox .administradoresConsAlter input[type='reset']").classList.remove("btnForm");
}



//Evento de aplicação de filtro por data
let filtroData = document.querySelector(".relatoriosBox .relatoriosFilters form select[name='data-desc']");
let datasInFin = document.querySelectorAll(".relatoriosBox .relatoriosFilters form input[type='date']");

if (filtroData) {

    datasInFin.forEach((input) => {
        input.value = "";
        input.disabled = true;
    });

    filtroData.addEventListener("change", () => {
        if (filtroData.value != "0") {
            datasInFin.forEach((input) => {
                input.disabled = false;
            });
        } else {
            datasInFin.forEach((input) => {
                input.value = "";
                input.disabled = true;
            });

			document.querySelector(".relatoriosBox .validationDatesForm").style.display = "none";
        }
    });

	document.querySelector(".relatoriosFilters .submit input").addEventListener("click", () => {
		
		let send = true;
		
		datasInFin.forEach((input) => {
	        if (input.disabled) {
				input.disabled = false;
			}
	    });

		if (filtroData.value != "0") {
			
			datasInFin.forEach((input) => {
                if (input.value == "") {
					document.querySelector(".relatoriosBox .validationDatesForm").innerHTML = "Especifique as datas!";
					document.querySelector(".relatoriosBox .validationDatesForm").style.display = "block";
					send = false;
				}
            });

			if (send) {
				let dataI = datasInFin[0].value.replace("-", " ");
				let dataF = datasInFin[1].value.replace("-", " ");
				
				if (dataI > dataF) {
					document.querySelector(".relatoriosBox .validationDatesForm").innerHTML = "A data inicial precisa ser menor que a data final!";
					document.querySelector(".relatoriosBox .validationDatesForm").style.display = "block";
					send = false;
				}
			}
			
		}
		
		if (send) document.querySelector(".relatoriosBox .relatoriosFilters form").submit();
		
	});

}