package lab.exceptions.labEngine;

import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;

public class ForcedRollBackWithResult extends RuntimeException {

    ResultSetWrappingSqlRowSet result;
    int rowCount;
    public ForcedRollBackWithResult(String message){
        super(message);
    }

    public ForcedRollBackWithResult(ResultSetWrappingSqlRowSet rowSet, int lastRowNumber) {
        this.rowCount = lastRowNumber;
        this.result = rowSet;
    }

    public ResultSetWrappingSqlRowSet getResult() {
        return result;
    }

    public void setResult(ResultSetWrappingSqlRowSet result) {
        this.result = result;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }
}

