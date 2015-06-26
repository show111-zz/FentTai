package com.huayjx.ftshiyao.    Cyfwdw;

    import android.content.Context;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;
    import android.widget.Toast;

    /** 信息列表 数据库
     * Created by lhf on 2015/6/7.
     */
    public class XxlbDatabase extends SQLiteOpenHelper {

        public static final String CREATE_INFOLIST = "create table Infolist(_id integer primary key autoincrement," +
            "ent_name text,tmp_oper_cate_name text,legal_person text,reg_address text," +
            "lastdate text,lic_no text,qylx text)";

        public static final String CREATE_YPINFO = "create table Infoyp(_id integer primary key autoincrement," +
              "qymc text,zcdz text,zsbh text,qyfzr text,ssfjmc text," +
             "qyid text,sqbbh text,ssfj text,yxqz text)";

        public static final String CREATE_LXRINFO = "create table Infolxr(_id integer primary key autoincrement," +
                "depart_name text,user_name text)";

        private Context context;

    public XxlbDatabase(Context context) {
        super(context, "InfoListTable.db", null, 2);//数据库名称
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_INFOLIST);
        db.execSQL(CREATE_YPINFO);
        db.execSQL(CREATE_LXRINFO);
        Toast.makeText(context,"建表成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Infolist");
        onCreate(db);
    }
}
