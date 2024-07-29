package lab.repository.labEngineRepo;

import lab.exceptions.labEngine.EmptyDataException;
import lab.exceptions.labEngine.ForcedRollBackWithResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LabEngineRepoImpl implements LabEngineRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean match(SqlRowSet rowSet1, SqlRowSet rowSet2) {
        int columnCount1 = rowSet1.getMetaData().getColumnCount();
        int columnCount2 = rowSet2.getMetaData().getColumnCount();
        if (columnCount1 != columnCount2)
            return false;

        boolean rs1next = rowSet1.next();
        boolean rs2next = rowSet2.next();

        while (rs1next && rs2next) {
            for (int i = 1; i <= columnCount1; i++) {
                Object val1 = rowSet1.getObject(i);
                Object val2 = rowSet2.getObject(i);
                if ((val1 == null && val2 != null) || (val1 != null && !val1.equals(val2))) {
                    return false;
                }
            }
            rs1next = rowSet1.next();
            rs2next = rowSet2.next();
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public SqlRowSet extract(String sqlQuery) {
        return jdbcTemplate.queryForRowSet(sqlQuery);
    }

    @Override
    @Transactional(rollbackFor = ForcedRollBackWithResult.class)
    public SqlRowSet executeAndExtract(String query, String tableName) {
        String selectQuery = "SELECT * FROM " + tableName + orderByClauseOnPrimaryKey(tableName);
        jdbcTemplate.update(query);
        ResultSetWrappingSqlRowSet rowSet = (ResultSetWrappingSqlRowSet) jdbcTemplate.queryForRowSet(selectQuery);
        int lastRowNumber = -1;
        try {
            rowSet.getResultSet().last();
            lastRowNumber = rowSet.getRow();
            rowSet.beforeFirst();
        } catch (SQLException e) {
        }

        throw new ForcedRollBackWithResult(rowSet, lastRowNumber);
    }

    @Override
    public void populateDisplayData(List<List<String>> outputList, SqlRowSet rowSet) {
        SqlRowSetMetaData metaData1 = rowSet.getMetaData();
        int columnCount = metaData1.getColumnCount();

        List<String> listOfColumnName = new ArrayList<>();
        for (int i = 1; i <= columnCount; i++) {
            listOfColumnName.add(metaData1.getColumnName(i));
        }
        if (listOfColumnName.isEmpty())
            throw new EmptyDataException("Please recheck your query!");
        else
            outputList.add(listOfColumnName);

        while (rowSet.next()) {
            List<String> temp = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                Object temp1 = rowSet.getObject(i);
                String val1 = (temp1 != null) ? rowSet.getObject(i).toString() : null;
                temp.add(val1);
            }
            outputList.add(temp);
        }
        rowSet.beforeFirst();
    }

    private String orderByClauseOnPrimaryKey(String tableName) {
        String sqlQuery = "SELECT ACC.COLUMN_NAME " +
                "FROM ALL_CONS_COLUMNS ACC " +
                "JOIN ALL_CONSTRAINTS AC ON ACC.CONSTRAINT_NAME = AC.CONSTRAINT_NAME " +
                "WHERE ACC.TABLE_NAME = ? " +
                "AND AC.TABLE_NAME = ? " +
                "AND AC.CONSTRAINT_TYPE = ?" +
                "AND ROWNUM = 1";
        String pk = jdbcTemplate.queryForObject(sqlQuery, new Object[]{tableName, tableName, "P"}, String.class);
        return " ORDER BY " + pk;
    }
}
