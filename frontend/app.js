const API = 'http://localhost:9090/api/v1/beneficios';

const refs = {
  form: document.getElementById('beneficioForm'),
  transferForm: document.getElementById('transferForm'),
  table: document.getElementById('beneficiosTable'),
  counter: document.getElementById('counter'),
  message: document.getElementById('message'),
  formTitle: document.getElementById('formTitle'),
  cancelEditBtn: document.getElementById('cancelEditBtn'),
  refreshBtn: document.getElementById('refreshBtn')
};

let beneficios = [];

const input = (id) => document.getElementById(id);

function showMessage(text, type = 'success') {
  refs.message.hidden = false;
  refs.message.className = `message ${type}`;
  refs.message.textContent = text;
}

function clearMessage() {
  refs.message.hidden = true;
  refs.message.textContent = '';
  refs.message.className = 'message';
}

function money(v) {
  return Number(v).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function resetForm() {
  input('beneficioId').value = '';
  refs.form.reset();
  input('ativo').checked = true;
  refs.formTitle.textContent = 'Novo benefício';
  refs.cancelEditBtn.hidden = true;
}

async function request(url, options = {}) {
  const res = await fetch(url, {
    headers: { 'Content-Type': 'application/json', ...(options.headers || {}) },
    ...options
  });

  if (!res.ok) {
    let message = 'Erro inesperado ao processar requisição';
    try {
      const body = await res.json();
      message = body.message || message;
    } catch {
      // ignore non-json
    }
    throw new Error(message);
  }

  if (res.status === 204) {
    return null;
  }

  return res.json();
}

function renderTable() {
  refs.table.innerHTML = '';
  refs.counter.textContent = `${beneficios.length} ${beneficios.length === 1 ? 'item' : 'itens'}`;

  beneficios.forEach((b) => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${b.id}</td>
      <td>${b.nome}</td>
      <td>${b.descricao || '-'}</td>
      <td>${money(b.valor)}</td>
      <td><span class="status ${b.ativo ? 'on' : 'off'}">${b.ativo ? 'ATIVO' : 'INATIVO'}</span></td>
      <td>
        <div class="row-actions">
          <button class="btn btn-secondary" data-action="edit" data-id="${b.id}">Editar</button>
          <button class="btn btn-danger" data-action="delete" data-id="${b.id}">Excluir</button>
        </div>
      </td>
    `;
    refs.table.appendChild(tr);
  });

  const options = beneficios
    .map((b) => `<option value="${b.id}">${b.nome} (ID ${b.id})</option>`)
    .join('');
  input('fromId').innerHTML = `<option value="">Selecione</option>${options}`;
  input('toId').innerHTML = `<option value="">Selecione</option>${options}`;
}

async function load() {
  clearMessage();
  beneficios = await request(API);
  renderTable();
}

refs.form.addEventListener('submit', async (e) => {
  e.preventDefault();
  clearMessage();

  const payload = {
    nome: input('nome').value.trim(),
    descricao: input('descricao').value.trim(),
    valor: Number(input('valor').value),
    ativo: input('ativo').checked
  };

  const id = input('beneficioId').value;

  try {
    if (id) {
      await request(`${API}/${id}`, { method: 'PUT', body: JSON.stringify(payload) });
      showMessage('Benefício atualizado com sucesso.');
    } else {
      await request(API, { method: 'POST', body: JSON.stringify(payload) });
      showMessage('Benefício criado com sucesso.');
    }

    await load();
    resetForm();
  } catch (err) {
    showMessage(err.message, 'error');
  }
});

refs.transferForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  clearMessage();

  const fromId = Number(input('fromId').value);
  const toId = Number(input('toId').value);
  const amount = Number(input('amount').value);

  try {
    await request(`${API}/transfer`, {
      method: 'POST',
      body: JSON.stringify({ fromId, toId, amount })
    });

    showMessage('Transferência realizada com sucesso.');
    refs.transferForm.reset();
    await load();
  } catch (err) {
    showMessage(err.message, 'error');
  }
});

refs.table.addEventListener('click', async (e) => {
  const button = e.target.closest('button[data-action]');
  if (!button) return;

  const action = button.dataset.action;
  const id = Number(button.dataset.id);

  if (action === 'edit') {
    const beneficio = beneficios.find((item) => item.id === id);
    if (!beneficio) return;

    input('beneficioId').value = beneficio.id;
    input('nome').value = beneficio.nome;
    input('descricao').value = beneficio.descricao || '';
    input('valor').value = beneficio.valor;
    input('ativo').checked = Boolean(beneficio.ativo);
    refs.formTitle.textContent = `Editando benefício #${beneficio.id}`;
    refs.cancelEditBtn.hidden = false;
    window.scrollTo({ top: 0, behavior: 'smooth' });
    return;
  }

  if (action === 'delete') {
    const confirmed = window.confirm(`Deseja realmente excluir o benefício ${id}?`);
    if (!confirmed) return;

    try {
      await request(`${API}/${id}`, { method: 'DELETE' });
      showMessage('Benefício removido com sucesso.');
      await load();
      resetForm();
    } catch (err) {
      showMessage(err.message, 'error');
    }
  }
});

refs.cancelEditBtn.addEventListener('click', () => {
  clearMessage();
  resetForm();
});

refs.refreshBtn.addEventListener('click', async () => {
  try {
    await load();
    showMessage('Lista atualizada com sucesso.');
  } catch (err) {
    showMessage(err.message, 'error');
  }
});

load().catch((err) => showMessage(err.message, 'error'));
