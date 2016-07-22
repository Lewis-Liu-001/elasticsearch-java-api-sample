package my.sample.elasticsearch.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tweet implements Serializable {

    private static final long serialVersionUID = 95795918207916987L;
    private String user;
    private Date postDate;
    private String message;

}
