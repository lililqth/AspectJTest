package com.aspectj.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Stack;

public class ValueTracker{

	public static HashMap<String, ArrayList<ValueChangePoint>> valueMap;
	
	public static void analysis(String filepath) throws IOException {
		valueMap = _analysis(filepath);
	}
	
	/** analysis
	 *  analysis the xml file to get all ValueChangePoint
	 * @param filepath the xml file's path
	 * @return Dictionary<ValueName, ArrayList<ValueChangepoint>>
	 * @throws IOException 
	 */
	private static HashMap<String, ArrayList<ValueChangePoint>> _analysis(String filepath) throws IOException {

			
			HashMap<String, ArrayList<ValueChangePoint>> result = new HashMap<>();
			Stack<String> functionStack = new Stack<String>();
			
			BufferedReader br = new BufferedReader( new FileReader(	new File(filepath) ) );
			
			String tmpString = br.readLine();
			
			// 针对每一行变化进行判断
			while (tmpString != null) {
				
				// 函数栈模拟
				if (tmpString.startsWith("<functionstart>")) {
					functionStack.push(tmpString.replace("<functionstart>", "").replace("</functionstart>", ""));					
				}
				else if (tmpString.startsWith("<functionend>")) {
					functionStack.pop();
				}
				
				// 将变量加入 result 集 
				else if (tmpString.startsWith("<value>")) {
					tmpString = tmpString.replace("<value>", "").replace("</value>", "");
					String valueName = tmpString.split("\\|\\|")[0];
					String value = tmpString.split("\\|\\|")[1];
					
					if (result.containsKey(valueName)) {
						ArrayList<ValueChangePoint> tmpArrayList = result.get(valueName);
						String OldValue = tmpArrayList.get(tmpArrayList.size() - 1).newValue;
						tmpArrayList.add(new ValueChangePoint(functionStack.peek(), OldValue, value));
					} else {
						ArrayList<ValueChangePoint> tmpArrayList = new ArrayList<ValueChangePoint>();
						if(!functionStack.isEmpty()){
							tmpArrayList.add(new ValueChangePoint(functionStack.peek(), null, value));
						}else{
							tmpArrayList.add(new ValueChangePoint("显式初始化", null, value));
						}
						
						result.put(valueName, tmpArrayList);
					}
				}
				tmpString = br.readLine();
			}
			
			br.close();
			
			return result;
		}

	public static ArrayList<ValueChangePoint> getValueList(String valueName)
	{
		// 如果没有分析过则报错
		if (valueMap == null) {
			throw new NullPointerException();
		}
		
		// 寻找变量并返回其 ArrayList
		else {
			Iterator<Entry<String, ArrayList<ValueChangePoint>>> it = valueMap.entrySet().iterator();
			
			// 遍历字典中的每个 Keys, 看看是否存在
			while (it.hasNext()) {
				Entry<String, ArrayList<ValueChangePoint>> entry = it.next();
				String keyName = entry.getKey();
				
				// 判断是否为 类.属性 的格式
				String startSymbol;
				if (valueName.contains(".")) {
					startSymbol = "";
				} else {
					startSymbol = ".";
				}
				
				// 寻找结果
				if (keyName.endsWith(startSymbol + valueName)) {
					return entry.getValue();
				}
			}
			
			// 不存在该变量则报错
			throw new NullPointerException();
		}
	}
}
