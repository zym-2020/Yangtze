package njnu.edu.back.common.utils;

import org.apache.ibatis.type.*;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/03/29/22:49
 * @Description:
 */
@MappedJdbcTypes(JdbcType.ARRAY)
@MappedTypes(String[].class)
public class ArrayTypeHandler extends BaseTypeHandler<Object[]> {

    private static final String ARRAY_TYPE_INTEGER = "integer";
    private static final String ARRAY_TYPE_VARCHAR = "varchar";
    private static final String ARRAY_TYPE_NUMERIC = "numeric";
    private static final String ARRAY_TYPE_BOOLEAN = "boolean";

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object[] objects, JdbcType jdbcType) throws SQLException {
        String typeName = null;
        if (objects instanceof Integer[]) {
            typeName = ARRAY_TYPE_INTEGER;
        } else if (objects instanceof String[]) {
            typeName = ARRAY_TYPE_VARCHAR;
        } else if (objects instanceof Boolean[]) {
            typeName = ARRAY_TYPE_NUMERIC;
        } else if (objects instanceof Double[]) {
            typeName = ARRAY_TYPE_BOOLEAN;
        }

        if (typeName == null) {
            throw new TypeException("ArrayTypeHandler parameter typeName error, your type is " + objects.getClass().getName());
        }
        // 这3行是关键的代码，创建Array，然后ps.setArray(i, array)就可以了
        Connection conn = preparedStatement.getConnection();
        Array array = conn.createArrayOf(typeName, objects);
        preparedStatement.setArray(i, array);

    }

    @Override
    public Object[] getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return getArray(resultSet.getArray(s));
    }

    @Override
    public Object[] getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return getArray(resultSet.getArray(i));
    }

    @Override
    public Object[] getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return getArray(callableStatement.getArray(i));
    }

    private Object[] getArray(Array array) {
        if (array == null) {
            return null;
        }
        try {
            return (Object[]) array.getArray();
        } catch (Exception e) {
        }
        return null;
    }

}
