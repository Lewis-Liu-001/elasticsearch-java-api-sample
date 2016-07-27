package my.sample.elasticsearch.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parent implements Serializable {

    private static final long serialVersionUID = 7197916139564354002L;
    private String id;
    private String name;
    private String description;
    private List<Child> children;

}
