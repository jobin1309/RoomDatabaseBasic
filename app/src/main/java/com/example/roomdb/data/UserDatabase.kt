package com.example.roomdb.data

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomdb.Converters
import com.example.roomdb.model.User


@Database(entities = [User::class], version = 3, exportSchema = false)

@TypeConverters(Converters::class)
abstract class UserDatabase: RoomDatabase() {




    abstract fun userDao(): UserDao;


    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null;


        //migration code , when changing the user table
        val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Perform your migration here

                database.execSQL("ALTER TABLE user_table RENAME COLUMN lastName TO location");

            }
        }

        val migration_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Perform your migration here

                database.execSQL("ALTER TABLE user_table  ADD COLUMN image BLOB DEFAULT null")

            }
        }


        fun getDatabase(context: Context): UserDatabase {
            val tempInstance = INSTANCE;
            if(tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                ).addMigrations(migration_1_2,migration_2_3).build()
                INSTANCE = instance
                return instance;

            }
        }
    }

}