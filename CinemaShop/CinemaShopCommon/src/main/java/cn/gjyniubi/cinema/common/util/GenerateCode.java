package cn.gjyniubi.cinema.common.util;

import cn.gjyniubi.cinema.common.config.durid.DruidConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * @Author gujianyang
 * @Date 2020/12/7
 * @Class GenerateCode
 */
@ConditionalOnBean(DruidConfig.class)
@Component
public class GenerateCode {

    @Value("${spring.datasource.druid.master.url}")
    private String url;
    @Value("${spring.datasource.druid.master.password}")
    private String password;
    @Value("${spring.datasource.druid.master.username}")
    private String username;

    public void generateCode(String table) throws SQLException {
        Connection connection=DriverManager.getConnection(url,username,password);
        ResultSet query = connection.createStatement().executeQuery("select * from " + table);
        ResultSetMetaData metaData = query.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i);
            if(columnName.contains("_")){
                StringBuilder sb=new StringBuilder();
                boolean nextBig=false;
                char[] charArray = columnName.toCharArray();
                System.out.println("\t@TableField(\""+columnName+"\")");
                for (int j = 0; j < charArray.length; j++) {
                    if(charArray[j]=='_'){
                        nextBig=true;
                        continue;
                    }
                    if(nextBig){
                        if(charArray[j]>='a'&&charArray[j]<='z'){
                            sb.append((char)(charArray[j]-32));
                        }else
                            sb.append(charArray[j]);
                        nextBig=false;
                        continue;
                    }
                    sb.append(charArray[j]);
                }
                columnName=sb.toString();
            }
            if(columnName.equals("status"))
                System.out.println("\t@TableField(\"`status`\")");
            else if(columnName.equalsIgnoreCase("logic_del"))
                System.out.println("@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)");
            switch (metaData.getColumnType(i)){
                case Types.INTEGER:
                case Types.SMALLINT:
                case Types.TINYINT:
                    System.out.println("\tprotected Integer "+columnName+";");
                    break;
                case Types.BIGINT:
                    System.out.println("\tprotected Long "+columnName+";");
                    break;
                case Types.FLOAT:
                case Types.DOUBLE:
                    System.out.println("\tprotected Double "+columnName+";");
                    break;
                case Types.NUMERIC:
                    System.out.println("\tprotected BigDecimal "+columnName+";");
                    break;
                case Types.LONGVARCHAR:
                case Types.LONGNVARCHAR:
                case Types.CHAR:
                case Types.VARCHAR:
                    System.out.println("\tprotected String "+columnName+";");
                    break;
                case Types.BIT:
                    System.out.println("\tprotected Byte "+columnName+";");
                    break;
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    System.out.println("\t@JsonFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")");
                    System.out.println("\tprotected Date "+columnName+";");
                    break;
                default:
                    System.out.println(columnName + " " + metaData.getColumnType(i));
                    break;
            }
        }
    }
}
