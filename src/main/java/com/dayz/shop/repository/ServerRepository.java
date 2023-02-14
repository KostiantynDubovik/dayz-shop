package com.dayz.shop.repository;

import com.dayz.shop.jpa.entities.Server;
import com.dayz.shop.jpa.entities.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long> {
	List<Server> findAllByStoreId(Long storeId);
	List<Server> findAllByStore(Store store);

	@Query(nativeQuery = true, value = "SELECT S.SERVER_NAME, SUM(O.ORDER_TOTAL) AS SERVER_REVENUE\n" +
			"FROM orders O\n" +
			"\t     JOIN servers S ON S.SERVER_ID = O.SERVER_ID\n" +
			"WHERE O.TIME_PLACED > :from\n" +
			"  AND O.TIME_PLACED < :to\n" +
			"  AND O.USER_ID != -1\n" +
			"  AND O.USER_ID NOT IN (SELECT USER_ID FROM payments where USER_FROM = -1)\n" +
			"  AND O.STORE_ID = :storeId\n" +
			"  AND O.STATUS = 'COMPLETE'\n" +
			"  GROUP BY O.SERVER_ID \n" +
			"UNION\n" +
			"SELECT 'TOTAL', SUM(O.ORDER_TOTAL) AS SERVER_REVENUE\n" +
			"FROM orders O\n" +
			"\t     JOIN servers S ON S.SERVER_ID = O.SERVER_ID\n" +
			"WHERE O.TIME_PLACED > :from\n" +
			"  AND O.TIME_PLACED < :to\n" +
			"  AND O.USER_ID != -1\n" +
			"  AND O.USER_ID NOT IN (SELECT USER_ID FROM payments where USER_FROM = -1)\n" +
			"  AND O.STORE_ID = :storeId\n" +
			"  AND O.STATUS = 'COMPLETE'\n" +
			"ORDER BY SERVER_REVENUE DESC\n")
	List<List<String>> getServersRevenueNoConstantine(Long storeId, LocalDate from, LocalDate to);

	@Query(nativeQuery = true, value = "SELECT S.SERVER_NAME, SUM(O.ORDER_TOTAL) AS SERVER_REVENUE\n" +
			"FROM orders O\n" +
			"\t     JOIN servers S ON S.SERVER_ID = O.SERVER_ID\n" +
			"WHERE O.TIME_PLACED > :from\n" +
			"  AND O.TIME_PLACED < :to\n" +
			"  AND O.STORE_ID = :storeId\n" +
			"  AND O.STATUS = 'COMPLETE'\n"+
			" GROUP BY O.SERVER_ID "+
			"UNION\n" +
			"SELECT 'TOTAL', SUM(O.ORDER_TOTAL) AS SERVER_REVENUE\n" +
			"FROM orders O\n" +
			"\t     JOIN servers S ON S.SERVER_ID = O.SERVER_ID\n" +
			"WHERE O.TIME_PLACED > :from\n" +
			"  AND O.TIME_PLACED < :to\n" +
			"  AND O.STORE_ID = :storeId\n" +
			"  AND O.STATUS = 'COMPLETE'\n" +
			"ORDER BY SERVER_REVENUE DESC\n" )
	List<List<String>> getServersRevenue(Long storeId, LocalDate from, LocalDate to);
}
