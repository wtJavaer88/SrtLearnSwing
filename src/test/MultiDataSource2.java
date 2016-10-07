package test;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.wnc.basic.BasicFileUtil;
import com.wnc.srtlearn.dao.DictionaryDao;
import com.wnc.srtlearn.modules.translate.Topic;

import db.DataSource;
import db.DbExecMgr;

public class MultiDataSource2 {
	public static void main(String[] args) {

		BasicFileUtil.deleteFile("srt-word-err.txt");

		DictionaryDao.initStatic();

		DbExecMgr.refreshCon(DataSource.SRT);
		Map selectAllSqlMap3 = DbExecMgr.getSelectAllSqlMap("SELECT * FROM SRTINFO");
		for (int i = 1; i <= selectAllSqlMap3.size(); i++) {
			Map rowMap = (Map) selectAllSqlMap3.get(i);
			String dialog = String.valueOf(rowMap.get("ENG"));
			Set<Topic> cetTopic = DictionaryDao.getCETTopic(dialog);
			String sql = null;
			for (Topic topic : cetTopic) {
				// System.out.println("" + topic.getTopic_word() + " " +
				// topic.getTopic_id() + " " + rowMap.get("ID"));
				try {
					sql = "INSERT INTO SRT_WORD(SRT_ID,TOPIC_ID,REAL_WORD) VALUES(" + rowMap.get("ID") + ","
							+ topic.getTopic_id() + ",'" + topic.getTopic_word() + "')";
					// System.out.println(sql);
					// DbExecMgr.refreshCon(DataSource.SRT);
					DbExecMgr.execOnlyOneUpdate(sql);
				} catch (SQLException e) {
					e.printStackTrace();
					BasicFileUtil.writeFileString("srt-word-err.txt", "SQL:" + sql + "  " + e.getMessage() + "\r\n",
							null, true);
				}
			}
		}
	}
}
