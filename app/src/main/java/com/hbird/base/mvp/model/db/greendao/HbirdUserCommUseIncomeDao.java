package com.hbird.base.mvp.model.db.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.hbird.base.mvp.model.entity.table.HbirdUserCommUseIncome;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HBIRD_USER_COMM_USE_INCOME".
*/
public class HbirdUserCommUseIncomeDao extends AbstractDao<HbirdUserCommUseIncome, Void> {

    public static final String TABLENAME = "HBIRD_USER_COMM_USE_INCOME";

    /**
     * Properties of entity HbirdUserCommUseIncome.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", false, "ID");
        public final static Property Icon = new Property(1, String.class, "icon", false, "ICON");
        public final static Property Priority = new Property(2, Integer.class, "priority", false, "PRIORITY");
        public final static Property IncomeName = new Property(3, String.class, "incomeName", false, "INCOME_NAME");
        public final static Property Mark = new Property(4, Integer.class, "mark", false, "MARK");
        public final static Property ParentId = new Property(5, String.class, "parentId", false, "PARENT_ID");
        public final static Property ParentName = new Property(6, String.class, "parentName", false, "PARENT_NAME");
        public final static Property AbTypeId = new Property(7, Integer.class, "abTypeId", false, "AB_TYPE_ID");
        public final static Property UserInfoId = new Property(8, Integer.class, "userInfoId", false, "USER_INFO_ID");
    }


    public HbirdUserCommUseIncomeDao(DaoConfig config) {
        super(config);
    }
    
    public HbirdUserCommUseIncomeDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HBIRD_USER_COMM_USE_INCOME\" (" + //
                "\"ID\" TEXT UNIQUE ," + // 0: id
                "\"ICON\" TEXT," + // 1: icon
                "\"PRIORITY\" INTEGER," + // 2: priority
                "\"INCOME_NAME\" TEXT," + // 3: incomeName
                "\"MARK\" INTEGER," + // 4: mark
                "\"PARENT_ID\" TEXT," + // 5: parentId
                "\"PARENT_NAME\" TEXT," + // 6: parentName
                "\"AB_TYPE_ID\" INTEGER," + // 7: abTypeId
                "\"USER_INFO_ID\" INTEGER);"); // 8: userInfoId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HBIRD_USER_COMM_USE_INCOME\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HbirdUserCommUseIncome entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(2, icon);
        }
 
        Integer priority = entity.getPriority();
        if (priority != null) {
            stmt.bindLong(3, priority);
        }
 
        String incomeName = entity.getIncomeName();
        if (incomeName != null) {
            stmt.bindString(4, incomeName);
        }
 
        Integer mark = entity.getMark();
        if (mark != null) {
            stmt.bindLong(5, mark);
        }
 
        String parentId = entity.getParentId();
        if (parentId != null) {
            stmt.bindString(6, parentId);
        }
 
        String parentName = entity.getParentName();
        if (parentName != null) {
            stmt.bindString(7, parentName);
        }
 
        Integer abTypeId = entity.getAbTypeId();
        if (abTypeId != null) {
            stmt.bindLong(8, abTypeId);
        }
 
        Integer userInfoId = entity.getUserInfoId();
        if (userInfoId != null) {
            stmt.bindLong(9, userInfoId);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HbirdUserCommUseIncome entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String icon = entity.getIcon();
        if (icon != null) {
            stmt.bindString(2, icon);
        }
 
        Integer priority = entity.getPriority();
        if (priority != null) {
            stmt.bindLong(3, priority);
        }
 
        String incomeName = entity.getIncomeName();
        if (incomeName != null) {
            stmt.bindString(4, incomeName);
        }
 
        Integer mark = entity.getMark();
        if (mark != null) {
            stmt.bindLong(5, mark);
        }
 
        String parentId = entity.getParentId();
        if (parentId != null) {
            stmt.bindString(6, parentId);
        }
 
        String parentName = entity.getParentName();
        if (parentName != null) {
            stmt.bindString(7, parentName);
        }
 
        Integer abTypeId = entity.getAbTypeId();
        if (abTypeId != null) {
            stmt.bindLong(8, abTypeId);
        }
 
        Integer userInfoId = entity.getUserInfoId();
        if (userInfoId != null) {
            stmt.bindLong(9, userInfoId);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public HbirdUserCommUseIncome readEntity(Cursor cursor, int offset) {
        HbirdUserCommUseIncome entity = new HbirdUserCommUseIncome( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // icon
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // priority
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // incomeName
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // mark
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // parentId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // parentName
            cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7), // abTypeId
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8) // userInfoId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, HbirdUserCommUseIncome entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setIcon(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPriority(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setIncomeName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setMark(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setParentId(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setParentName(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAbTypeId(cursor.isNull(offset + 7) ? null : cursor.getInt(offset + 7));
        entity.setUserInfoId(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(HbirdUserCommUseIncome entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(HbirdUserCommUseIncome entity) {
        return null;
    }

    @Override
    public boolean hasKey(HbirdUserCommUseIncome entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
