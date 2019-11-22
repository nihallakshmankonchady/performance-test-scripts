package app.dbaccess;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Session;

import app.entity.RegistrationStatusEntity;
import app.entity.TransactionEntity;

public class DBAccessImpl {

	public List<TransactionEntity> getRegTransactionForRegId(Session session, List<String> regidList) {
		List<TransactionEntity> result = null;

		String query = "from TransactionEntity trEntity where reg_id in (";

		for (String s : regidList) {
			query += "'" + s + "',";
		}

		query.substring(0, query.length() - 1);

		query += ") order by trEntity.createDateTime";

		Query q = session.createQuery(query);
		result = q.getResultList();

		return result;
	}

	public List<TransactionEntity> getRegTransactionForRegId(Session session, String regid) {
		List<TransactionEntity> list = null;
		String query = "from TransactionEntity trEntity where reg_id='" + regid + "' order by trEntity.createDateTime";

		Query q = session.createQuery(query);
		list = q.getResultList();
		return list;
	}

	public List<TransactionEntity> getTransactionsForRegIds(List<String> regIds) {
		List<TransactionEntity> list = null;
		return list;
	}

	public Long getAggregateTimeTakenForRegistrationId(Session session, String regid) throws Exception {
		long timeDifference = 0L;
		try {
			String query = "from RegistrationStatusEntity regEntity where id='" + regid + "'";

			Query q = session.createQuery(query);
			List<RegistrationStatusEntity> list = q.getResultList();
			RegistrationStatusEntity regEntity = list.get(0);

			LocalDateTime date1 = regEntity.getCreateDateTime();
			LocalDateTime date2 = regEntity.getUpdateDateTime();

			long localDTInMilli1 = date1.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
			long localDTInMilli2 = date2.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

			timeDifference = localDTInMilli2 - localDTInMilli1;

			return timeDifference;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex);
		}
		// return timeDifference;
	}

}
