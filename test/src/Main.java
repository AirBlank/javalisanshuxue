
import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        test test = new test();
        test.init();
        test.start();
    }
}

class test{
    private Map<String,Boolean> map = new HashMap<String, Boolean>();
    private String [][] chart =null;
    private Scanner sc = new Scanner(System.in);
    private Map<Integer,Character> tempMap = new HashMap<>();
    private String[] split = null;
    private int tempFlag = 0;
    private int tempFlag2 = 2;

    public void init(){
        map.put("p",true);
        map.put("!p",false);
        map.put("q",true);
        map.put("!q",false);
        map.put("r",true);
        map.put("!r",false);
    }

    public void start(){
        int row,column;
        String result = sc.nextLine();
        System.out.println(result);
        if (result.contains("p") && result.contains("q") && !result.contains("r")){
            //两个命题变元对应4行3列，外加一行列名
            chart = new String[5][3];
            initDefaultChartValue(chart,5,3,result);
            row = 5;
            column =3;
        }else {
            //三个命题变元对应8行4列，外加一行列名
            chart = new String[9][4];
            initDefaultChartValue(chart,9,4,result);
            row = 9;
            column = 4;
        }

        /*
         *    !代表非
         *    /代表或
         *    &代表且
         *    >代表若..则..
         *    -代表当且仅当
         *
         * */

        char[] chars = result.toCharArray();
        int flag = 0;
        for (int i = 0; i< chars.length;i++){
            char c = chars[i];
            if (c == '/' || c == '&' || c == '>' || c == '-'){
                tempMap.put(flag++,c);
                chars[i] = ' ';
            }
        }
        //将字符数组转成字符串再分割，得到命题变元
        String a = new String(chars);
        System.out.println(a);
        //split就是命题变元
        split = a.split(" ");

//        for(String s : split){
//            System.out.println(s);
//        }
//        Stream.of(tempMap).forEach(x-> System.out.println(x));

        Map<String,Boolean> trueorfalseMap = new HashMap<>();
        String tempTorF = null;
        boolean tempBoolean;
        for(int j =1 ;j<row; j++){
            for (int k = 0 ;k < column-1 ;k++){
                //这里最多获取前2列或前3列的列名，最后一列是输入的表达式
                //先得到对应真值表的列名头和对应的真假值
                tempTorF = chart[j][k];
                if(tempTorF.toUpperCase().equals("T")){
                    tempBoolean = true;
                }else {
                    tempBoolean = false;
                }
                //把对应的真假值保存到trueorfalseMap中
                trueorfalseMap.put(chart[0][k],tempBoolean);
                //同时放入相反的值
                trueorfalseMap.put("!"+chart[0][k],!tempBoolean);
                //记住，后面用完后一定要清空，用来给第二轮用，避免key冲突
            }
            //查看扫描是否正确
            trueorfalseMap.forEach((x,y)-> System.out.print(x+" "+y));
            System.out.println();
            //开始计算
            //先取出符号
            //boolResult默认为true
            boolean boolResult = true;
            for (int i = 0; i< tempMap.size();i++){
                String operator = Character.toString(tempMap.get(i));
                System.out.println(operator);
                boolResult = judge(operator,boolResult,split,trueorfalseMap);
                tempFlag++;
                chart[j][column-1] = new Boolean(boolResult).toString().substring(0,1).toUpperCase();
            }
            tempFlag = 0;
            tempFlag2 = 2;
            trueorfalseMap.clear();
        }
        getChart(chart,row,column);
        getTwoExpression(chart,row,column);
    }

    private boolean judge(String operator,boolean boolResult,String[] split,Map<String,Boolean> trueorfalseMap){
        boolean tempBoolResult = boolResult;
        int test=0;
        //第一次进行判断
        if (tempFlag == 0){
            System.out.println("tempFlag"+tempFlag);
            if (operator.equals("/")){
                String var1 = split[0];
                String var2 = split[1];
                boolean b1,b2;
                b1 = trueorfalseMap.get(var1);
                b2 = trueorfalseMap.get(var2);
                if (b1 == false && b2 ==false){
                    tempBoolResult = false;
                }else {
                    tempBoolResult = true;
                }
                System.out.println(tempBoolResult);
                return tempBoolResult;
            }
            if (operator.equals("&")){
                String var1 = split[0];
                String var2 = split[1];
                boolean b1,b2;
                b1 = trueorfalseMap.get(var1);
                b2 = trueorfalseMap.get(var2);
                if (b1 == false && b2 ==true){
                    tempBoolResult = true;
                }else {
                    tempBoolResult = false;
                }
                return tempBoolResult;
            }
            if (operator.equals(">")){
                String var1 = split[0];
                String var2 = split[1];
                boolean b1,b2;
                b1 = trueorfalseMap.get(var1);
                b2 = trueorfalseMap.get(var2);
                if (b1 == true && b2 ==false){
                    tempBoolResult = false;
                }else {
                    tempBoolResult = true;
                }
                return tempBoolResult;
            }
            if (operator.equals("-")){
                String var1 = split[0];
                String var2 = split[1];
                boolean b1,b2;
                b1 = trueorfalseMap.get(var1);
                b2 = trueorfalseMap.get(var2);
                if (b1 == true && b2 ==true){
                    tempBoolResult = true;
                }else if (b1 == false && b2 ==false){
                    tempBoolResult = true;
                }else {
                    tempBoolResult = false;
                }
                return tempBoolResult;
            }
        }else {
            System.out.println("第"+ ++test + "进入else");
            System.out.println(split.length);
            System.out.println(tempFlag2);
            System.out.println(split[tempFlag2]);
            //不是第一次进行判断
            if (operator.equals("/")){
                String var1 = split[tempFlag2];
                tempFlag2++;
                boolean b1;
                b1 = trueorfalseMap.get(var1);
                if (boolResult == false && b1 ==false){
                    tempBoolResult = false;
                }else {
                    tempBoolResult = true;
                }
                return tempBoolResult;
            }
            if (operator.equals("&")){
                String var1 = split[tempFlag2];
                tempFlag2++;
                boolean b1;
                b1 = trueorfalseMap.get(var1);
                if (boolResult == true && b1 ==true){
                    tempBoolResult = true;
                }else {
                    tempBoolResult = false;
                }
                System.out.println(tempBoolResult);
                return tempBoolResult;
            }
            if (operator.equals(">")){
                String var1 = split[tempFlag2];
                tempFlag2++;
                boolean b1;
                b1 = trueorfalseMap.get(var1);
                if (boolResult == true && b1 ==false){
                    tempBoolResult = false;
                }else {
                    tempBoolResult = true;
                }
                return tempBoolResult;
            }
            if (operator.equals("-")){
                String var1 = split[tempFlag2];
                tempFlag2++;
                boolean b1;
                b1 = trueorfalseMap.get(var1);
                if (boolResult == true && b1 ==true){
                    tempBoolResult = true;
                }else if(boolResult == false && b1 ==false){
                    tempBoolResult = true;
                }else {
                    tempBoolResult = false;
                }
                return tempBoolResult;
            }
        }
        return tempBoolResult;
    }

    private void getTwoExpression(String [][] chart,int row,int column){
        StringBuilder sb1 = new StringBuilder();
        //主析取范式
        //即真值为T的项的pqr为T时的合取的析取
        for (int i = 1;i<row;i++){
            if (chart[i][column-1].equals("T")){
                 for (int j=0;j<column-1;j++){
                    //此时算最后一列即可
                    if (chart[i][j].equals("T")){
                        //如果这一个是T
                        if (j != column-2){
                            sb1.append(chart[0][j]);
                            sb1.append("&");
                        }else {
                            sb1.append(chart[0][j]);
                        }
                    }else {
                        //如果这一个是F
                        if (j != column-2){
                            sb1.append("!"+chart[0][j]);
                            sb1.append("&");
                        }else {
                            sb1.append("!"+chart[0][j]);
                        }
                    }
                }
                sb1.append("/");
            }
        }
        sb1.deleteCharAt(sb1.length()-1);
        System.out.println("主析取范式 : "+sb1);
        //清空sb1给下面继续用
        sb1.delete(0,sb1.length());
        //主合取范式
        //即真值为F的项的pqr为F时的析取的合取
        for (int i = 1;i<row;i++){
            if (chart[i][column-1].equals("F")){
                for (int j=0;j<column-1;j++){
                    //此时算最后一列即可
                    if (chart[i][j].equals("F")){
                        //如果这一个是T
                        if (j != column-2){
                            sb1.append(chart[0][j]);
                            sb1.append("/");
                        }else {
                            sb1.append(chart[0][j]);
                        }
                    }else {
                        //如果这一个是F
                        if (j != column-2){
                            sb1.append("!"+chart[0][j]);
                            sb1.append("/");
                        }else {
                            sb1.append("!"+chart[0][j]);
                        }
                    }
                }
                sb1.append("&");
            }
        }
        sb1.deleteCharAt(sb1.length()-1);
        System.out.println("主合取范式 : "+sb1);
    }

    private void initDefaultChartValue(String [][] chart,int row,int column,String result){
        if (column == 3){
            //对列名进行赋值
            chart[0][0] = "p";
            chart[0][1] = "q";
            chart[0][2] = result;
            //对列名下的每一个值进行赋值
            initValueTopqr("p",row,0);
            initValueTopqr("q",row,1);
        }else{
            chart[0][0] = "p";
            chart[0][1] = "q";
            chart[0][2] = "r";
            chart[0][3] = result;
            initValueTopqr("p",row,0);
            initValueTopqr("q",row,1);
            initValueTopqr("r",row,2);
        }
        getChart(chart,row,column);

    }

    private void initValueTopqr(String s,int row,int columnValue){
        //j代表第几行
        //columnValue代表第几列
        //row = 5 或 9
        if ("p".equals(s)){
            for (int j =1;j<row;j++){
                if (j < (row+1)/2){
                    chart[j][columnValue] = map.get(s).toString().substring(0,1).toUpperCase();
                }
                else{
                    chart[j][columnValue] = map.get("!"+s).toString().substring(0,1).toUpperCase();
                }
            }
        }else if("q".equals(s)){
            if (row == 5){
                for (int j =1;j<row;j++){
                    if (j == 1 || j ==3){
                        chart[j][columnValue] = map.get(s).toString().substring(0,1).toUpperCase();
                    }
                    else{
                        chart[j][columnValue] = map.get("!"+s).toString().substring(0,1).toUpperCase();
                    }
                }
            }else {
                for (int j =1;j<row;j++){
                    if (j == 1 || j ==2 || j==5 ||j ==6){
                        chart[j][columnValue] = map.get(s).toString().substring(0,1).toUpperCase();
                    }
                    else{
                        chart[j][columnValue] = map.get("!"+s).toString().substring(0,1).toUpperCase();
                    }
                }
            }

        }else {
            for (int j =1;j<row;j++){
                if (Util.conditionSecond(j)){
                    chart[j][columnValue] = map.get(s).toString().substring(0,1).toUpperCase();
                }
                else{
                    chart[j][columnValue] = map.get("!"+s).toString().substring(0,1).toUpperCase();
                }
            }
        }


    }

    private void getChart(String [][] chart,int row,int column){
        for (int i =0;i<row;i++){
            for (int j =0;j<column;j++){
                System.out.print(chart[i][j]+"\t");
            }
            System.out.println();
        }
    }
}

class Util{
    public static boolean conditionSecond(int i){
        if ( i % 2 != 0 ){
            return true;
        }
        return false;
    }
}


class exercise{
    private Map<String,String> map = new HashMap<String, String>();
    private List<String> strings = new ArrayList<>();
    //将二维数组作为表的结构
    private String [][] chart = null;

    //先存放对应的真和假的值
    public void init(){
        map.put("p","T");
        map.put("!p","F");
        map.put("q","T");
        map.put("!q","F");
        map.put("r","T");
        map.put("!r","F");
    }

    public void scan(){
        /*
        *    !代表非
        *    /代表或
        *    &代表且
        *    >代表若..则..
        *    -代表当且仅当
        * */
        Scanner sc = new Scanner(System.in);
        String result = sc.nextLine();
        System.out.println(result);
        //获得真值表的列名
        if (result.contains("p")){
            strings.add("p");
        }
        if (result.contains("!p")){
            strings.add("!p");
        }
        if (result.contains("q")){
            strings.add("q");
        }
        if (result.contains("!q")){
            strings.add("!q");
        }
        if (result.contains("r")){
            strings.add("r");
        }
        if (result.contains("!r")){
            strings.add("!r");
        }
        if (result.contains(">")){
            String[] split = result.split(">");
            Arrays.stream(split).forEach(x -> strings.add(x));
        }
        if (result.contains("-")){
            String[] split = result.split("-");
            Arrays.stream(split).forEach(x -> strings.add(x));
        }

        //最后把结果加进去
        strings.add(result);

        System.out.println(strings);
        //只有两个变量
        if (result.contains("p")&& result.contains("q")){
            chart = new String[5][strings.size()];
            judge(chart,5);
        }else {
            //3个变量
            chart = new String[9][strings.size()];
            judge(chart,9);
        }

    }

    private void judge(String [][] chart,int length){
        for (int i =0;i<strings.size();i++){
            //对二维数组的第一行进行赋值，形成表的初步结构
            chart[0][i] = strings.get(i);
        }

        //开始对对应的列进行赋值
        //注意这里是每一列每一列来
        for (int i =0;i<strings.size();i++){
            //i = 0 遍历第一行的每一个列
            initValue("p",i,length);
            initValue("!p",i,length);
            initValue("q",i,length);
            initValue("!q",i,length);
            initValue("r",i,length);
            initValue("!r",i,length);
        }

        for (int i=0;i<length;i++){
            for (int j = 0;j<strings.size();j++){
                System.out.print(chart[i][j]+"\t");
            }
            System.out.println();
        }
    }

    private void initValue(String s,int i,int length){
        //i是指列，length指行
        if(s.equals(chart[0][i])){
            for (int j=1;j<length;j++){
                //2或4
                if (j < (length+1) / 2){
                    chart[j][i] = map.get(s);
                }else {
                    if (s.contains("!")){
                        s = s.substring(1);
                        chart[j][i] = map.get(s);
                    }
                    else {
                        chart[j][i] = map.get("!"+s);
                    }
                }
            }
        }
    }
}