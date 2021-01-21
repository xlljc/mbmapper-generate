package org.mbmapper.produce.table;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableVo {

    private String name;

    private Map<String, String> fields;

    private List<String> imports;

    private Map<String, String> methods;



}
