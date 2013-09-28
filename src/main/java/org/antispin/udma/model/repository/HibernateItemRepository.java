package org.antispin.udma.model.repository;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.antispin.udma.model.GemQuality;
import org.antispin.udma.model.GemType;
import org.antispin.udma.model.IGem;
import org.antispin.udma.model.IItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public final class HibernateItemRepository extends AbstractItemRepository {

	final SessionFactory sessionFactory;
	final Session session;
	
	public HibernateItemRepository() {
		sessionFactory = new Configuration().configure().buildSessionFactory();
		session = sessionFactory.openSession();
	}
	
	public void close() {
		try {
			session.close();
		} finally {
			sessionFactory.close();
		}
	}
	
	public void add(IItem item) {
		final Transaction t = session.beginTransaction();
		session.persist(item);
		t.commit();
		notifyRepositoryChange();
	}

	public List<IGem> getGems(GemType gemType, GemQuality gemQuality) {
		throw new UnsupportedOperationException();
	}

	public void removeAll(Collection<? extends IItem> items) {
		for(IItem item: items) {
			session.delete(item);
		}
		notifyRepositoryChange();
	}

	public int size() {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray() {
		return session.createQuery("from Item order by itemCode").list().toArray();
	}

	public Iterator<IItem> iterator() {
		throw new UnsupportedOperationException();
	}

}
