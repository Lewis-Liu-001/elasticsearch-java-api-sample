package my.sample.elasticsearch.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Child implements Serializable{

    private static final long serialVersionUID = 3322042096842229962L;
    private String id;
    private String name;
    private String description;

}
