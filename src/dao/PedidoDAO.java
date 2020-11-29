package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import entidades.Pedido;
import model.PedidoVO;
import utilitarios.CalculadoraTotal;
import utilitarios.FabricaConexao;
import utilitarios.GeradorID;


public class PedidoDAO {
	private EntityManager em = FabricaConexao.getConexao();

	public boolean incluir(PedidoVO vo) {
		// utiliza um objeto de transferência (VO)
		Pedido p = new Pedido();
		p.setId(GeradorID.geraNumeroID());
		p.setNomeProduto(vo.getNomeProduto());
		p.setQuantidade(vo.getQuantidade());
		p.setValorTotal(CalculadoraTotal.calculaTotal(p.getQuantidade(),p.getNomeProduto()));
		p.setIdCliente(GeradorID.getId());
		
		vo.setId(p.getId()); // repassa o ID para o VO
		vo.setIdCliente(p.getIdCliente()); // repassa o ID do cliente para o VO

		/*/ trata os objetos de pedidos (formato "vo" para entity)
		List<PedidoVO> pedidosVO = vo.getPedidos();
		List<Pedido> pedidosEntity = new ArrayList<Pedido>();
		for (PedidoVO voPed : pedidosVO) {
			Pedido novoPedido = new Pedido();
			// o "id" é criado em Pedido()
			novoPedido.setData(voPed.getData());
			novoPedido.setNomeProduto(voPed.getNomeProduto());
			novoPedido.setQuantidade(voPed.getQuantidade());
			novoPedido.setValorTotal(voPed.getValorTotal());
			pedidosEntity.add(novoPedido);
		}
		c.setPedidos(pedidosEntity);
									*/
		em.getTransaction().begin();
		em.persist(p);
		em.getTransaction().commit();
		return true;
	}

	/**
	 * Pesquisa de Pedido por "id"
	 */
	public Pedido findById(Integer id) {
		Pedido p = em.find(Pedido.class, id);
		if (p == null) {
			throw new EntityNotFoundException("Pedido não localizado pelo ID " + id);
			
		}
		return p;
	}

	/**
	 * Recupera todos os pedidos da base de dados 
	 * @return uma Lista de Pedidos
	 */
	public List<Pedido> getPedidos() {
		Query  query = em.createQuery("select p from Pedido p", Pedido.class);
		return query.getResultList();
	}

	/**
	 * Este método realiza o "update" dos dados do cliente.
	 * @param vo
	 */
	public boolean atualiza(PedidoVO vo) {
		// recupera o objeto persistente
		Pedido p = this.findById(vo.getId());
		p.setNomeProduto(vo.getNomeProduto());
		p.setQuantidade(vo.getQuantidade());

		/*/ trata os objetos de pedidos (formato "vo" para entity)
		List<PedidoVO> pedidoVO = vo.getPedido();
		List<Pedido> pedidosEntity = new ArrayList<Pedido>();
		for (PedidoVO voPed : pedidosVO) {
			Pedido novoPedido = new Pedido();
			// o "id" é criado em Pedido()
			novoPedido.setData(voPed.getData());
			novoPedido.setNomeProduto(voPed.getNomeProduto());
			novoPedido.setQuantidade(voPed.getQuantidade());
			novoPedido.setValorTotal(voPed.getValorTotal());
			pedidosEntity.add(novoPedido);
		}
		c.setPedidos(pedidosEntity);*/

		em.getTransaction().begin();
		em.merge(p); // merge -> faz o "update" no objeto persistente
		em.getTransaction().commit();
		return true;
	}

	/**
	 * Este método deleta um cliente já cadastrado.
	 * @param vo
	 */
	public boolean delete(PedidoVO vo) {
		// recupera o objeto persistente
		Pedido p = this.findById(vo.getId());
		if (p == null)
			return false;

		em.getTransaction().begin();
		em.remove(p);  // remove -> faz o "delete" no objeto persistente
		em.getTransaction().commit();
		return true;
	}

	// deleta usando o ID
	public boolean delete(int id) {
		// recupera o objeto persistente
		Pedido p = this.findById(id);
		if (p == null)
			return false;
		em.getTransaction().begin();
		em.remove(p);  // remove -> faz o "delete" no objeto persistente
		em.getTransaction().commit();
		return true;
	}

}

