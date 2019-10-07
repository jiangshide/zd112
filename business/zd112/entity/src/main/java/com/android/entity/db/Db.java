package com.android.entity.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.android.entity.dao.ChannelDao;
import com.android.entity.dao.ContentTypeDao;
import com.android.entity.entity.Channel;
import com.android.entity.entity.ContentType;
import com.android.utils.AppUtil;

/**
 * created by jiangshide on 2019-10-04.
 * email:18311271399@163.com
 */
@Database(entities = {Channel.class,ContentType.class}, version = 3, exportSchema = false)
public abstract class Db extends RoomDatabase {

  private static final String DbName = "db.db";

  private static class DbHolder {
    private static Db instance = create();
  }

  public static Db getInstance() {
    return DbHolder.instance;
  }

  private static Db create() {
    return Room.databaseBuilder(AppUtil.getApplicationContext(), Db.class,DbName)
        .addCallback(new Callback() {
          @Override public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
          }

          @Override public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
          }
        })
        .addMigrations(new DbMigration(1, 2), new DbMigration(2, 3))
        .allowMainThreadQueries()
        .build();
  }

  public abstract ChannelDao getChannelDao();
  public abstract ContentTypeDao getContentTypeDao();

  private static class DbMigration extends Migration {

    /**
     * Creates a new migration between {@code startVersion} and {@code endVersion}.
     *
     * @param startVersion The start version of the database.
     * @param endVersion The end version of the database after this migration is applied.
     */
    public DbMigration(int startVersion, int endVersion) {
      super(startVersion, endVersion);
    }

    @Override public void migrate(@NonNull SupportSQLiteDatabase database) {
      switch (startVersion) {
        case 1:
          break;
        case 2:
          break;
      }
    }
  }
}
