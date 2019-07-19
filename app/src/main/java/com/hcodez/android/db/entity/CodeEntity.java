package com.hcodez.android.db.entity;

import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

import java.net.URL;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "code")
public class CodeEntity {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "identifier")
    public String identifier;

    @ColumnInfo(name = "owner")
    public String owner;

    @ColumnInfo(name = "passcode")
    public String passcode;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "url")
    private URL url;

    @ColumnInfo(name = "create_time")
    private Instant createTime;

    @ColumnInfo(name = "update_time")
    private Instant updateTime;

    @ColumnInfo(name = "code_type")
    private CodeType codeType;
}
