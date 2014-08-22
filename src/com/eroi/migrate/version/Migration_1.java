package com.eroi.migrate.version;

import java.sql.SQLException;

import com.eroi.migrate.AbstractMigration;
import com.eroi.migrate.ConfigStore;
import com.eroi.migrate.generators.GenericGenerator;

/**
 * Create version table via migration. 
 * We don't have a version for the version table, so we check whether the version table exists
 * 
 */
public class Migration_1 extends AbstractMigration {

	private String versionTableNew;

	public void init() {
		this.versionTableNew = config.getFullQualifiedVersionTable();
	}

	public void up() {
		if (! tableExists(this.versionTableNew)) {
			
			// create new version table with prefixed name and two columns: project (PK), version
			boolean oldUpperCaseValue = GenericGenerator.getChangeCaseToUpper();
			GenericGenerator.setChangeCaseToUpper(false);
			createTable(
					table(	this.versionTableNew, 
							column(ConfigStore.PROJECT_FIELD_NAME, VARCHAR, length(200), primarykey()), 
							column(ConfigStore.VERSION_FIELD_NAME, INTEGER)));
			GenericGenerator.setChangeCaseToUpper(oldUpperCaseValue);
			try
			{
				this.getConfiguration().getConnection().commit();
			}
			catch (SQLException e)
			{
				
			}
		}
	}

	public void down() {
		dropTable(this.versionTableNew);
	}

}
