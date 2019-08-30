package com.hcodez.android.db.entity;

import com.hcodez.codeengine.model.Code;
import com.hcodez.codeengine.model.CodeType;
import com.hcodez.codeengine.model.MutableCode;

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
                childColumns = "content_id",
                onDelete = ForeignKey.CASCADE
        ))
public class CodeEntity implements Code {

    @PrimaryKey
    private Integer id;

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
    private Integer contentId;


    /**
     * Create a library Code model from a CodeEntity
     * @return the Code model built
     */
    public Code toCodeInterface() {
        return MutableCode.builder()
                .identifier(this.getIdentifier())
                .owner(this.getOwner())
                .passcode(this.getPasscode())
                .codeType(this.getCodeType())
                .build();
    }

    /**
     * Create a CodeEntity from a library Code model
     * @param codeInterface the library Code model used
     * @return the CodeEntity built
     */
    public static CodeEntity fromCodeInterface(final Code codeInterface) {
        return CodeEntity.builder()
                .identifier(codeInterface.getIdentifier())
                .owner(codeInterface.getOwner())
                .passcode(codeInterface.getPasscode())
                .codeType(codeInterface.getCodeType())
                .build();
    }

    @Override
    public String toString() {
        return Code.string(this);
    }
}
