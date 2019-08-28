package com.hcodez.android.db.entity;

import com.hcodez.codeengine.builder.CodeBuilder;
import com.hcodez.codeengine.model.Code;
import com.hcodez.codeengine.model.CodeType;

import org.joda.time.Instant;

import java.net.URL;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(tableName = "code",
        foreignKeys = @ForeignKey(
                entity = ContentEntity.class,
                parentColumns = "id",
                childColumns = "content_id"
        ))
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

    @ColumnInfo(name = "content_id", index = true)
    private int contentId;


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
        return CodeEntity.builder()
                .identifier(libraryCode.getIdentifier())
                .owner(libraryCode.getOwner())
                .passcode(libraryCode.getPasscode())
                .name(libraryCode.getName())
                .url(libraryCode.getUrl())
                .createTime(libraryCode.getCreateTime())
                .updateTime(libraryCode.getUpdateTime())
                .codeType(libraryCode.getCodeType())
                .build();
    }
}
