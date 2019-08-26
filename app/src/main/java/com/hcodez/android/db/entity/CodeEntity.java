package com.hcodez.android.db.entity;

import com.hcodez.codeengine.builder.CodeBuilder;
import com.hcodez.codeengine.model.Code;
import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

import java.net.URL;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(tableName = "code")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CodeEntity {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "identifier")
    private String identifier;

    @ColumnInfo(name = "owner")
    private String owner;

    @ColumnInfo(name = "passcode")
    private String passcode;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public CodeType getCodeType() {
        return codeType;
    }

    public void setCodeType(CodeType codeType) {
        this.codeType = codeType;
    }

    /**
     * Create a library Code model from a CodeEntity
     * @return the Code model built
     */
    public Code toLibraryCode() {
        return CodeBuilder.createBuilder()
                .withIdentifier(this.getIdentifier())
                .withOwner(this.getOwner())
                .withPasscode(this.getPasscode())
                .withName(this.getName())
                .withUrl(this.getUrl())
                .withCreateTime(this.getCreateTime())
                .withUpdateTime(this.getUpdateTime())
                .withCodeType(this.getCodeType())
                .build();
    }

    /**
     * Create a CodeEntity from a library Code model
     * @param libraryCode the library Code model used
     * @return the CodeEntity built
     */
    public static CodeEntity fromLibraryCode(final Code libraryCode) {
        final CodeEntity codeEntity = new CodeEntity();

        codeEntity.setIdentifier(libraryCode.getIdentifier());
        codeEntity.setOwner(libraryCode.getOwner());
        codeEntity.setPasscode(libraryCode.getPasscode());
        codeEntity.setName(libraryCode.getName());
        codeEntity.setUrl(libraryCode.getUrl());
        codeEntity.setCreateTime(libraryCode.getCreateTime());
        codeEntity.setUpdateTime(libraryCode.getUpdateTime());
        codeEntity.setCodeType(libraryCode.getCodeType());

        return codeEntity;
    }
}
