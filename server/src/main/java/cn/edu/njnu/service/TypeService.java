package cn.edu.njnu.service;

import cn.edu.njnu.mapper.TypeMapper;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TypeService {
    private final TypeMapper typeMapper;

    public TypeService(TypeMapper typeMapper) {
        this.typeMapper = typeMapper;
    }

    public Result classification(){
        JSONArray resArray = new JSONArray();
        resArray.add(kemu());
        return ResultFactory.buildSuccessResult("Success", resArray);
    }

    public JSONObject kemu(){
        JSONObject resObject = new JSONObject();
        resObject.put("condition", "资源类别");
        List<Map> periodList = typeMapper.period();
        JSONArray resArray = new JSONArray();
        for (Map period:periodList){
            JSONObject periodOb = new JSONObject();
            String periodID = Integer.toString((Integer) period.get("period_id"));
            String periodName = (String) period.get("period_name");
            periodOb.put("ID", Integer.parseInt(periodID));
            periodOb.put("name", periodName);
            JSONArray subjectArray = new JSONArray();
            List<Map> subjectList = typeMapper.subject(periodID);
            for (Map subject:subjectList){
                JSONObject subjectOb = new JSONObject();
                int subjectID = (int) subject.get("subject_id");
                String subjectName = (String) subject.get("subject_name");
                subjectOb.put("menuID", subjectID);
                subjectOb.put("menuName", subjectName);
                subjectArray.add(subjectOb);
            }
            periodOb.put("menu", subjectArray);
            resArray.add(periodOb);
        }
        resObject.put("classification", resArray);
        return resObject;
    }
}
