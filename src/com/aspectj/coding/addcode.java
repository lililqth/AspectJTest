package com.aspectj.coding;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.aspectj.demo.Editor;

public class addcode {
	private static int count = 0;
	private static int undoCount = 0;

	public static int getcount() {
		return count;
	}

	public static void setCount(int count) {
		addcode.count = count;
	}

	public static void writeaj(String pathname, String Active, String Way, String selectfuction[], String content)
			throws IOException {
		if (Active.equals("5")) {
			count++;
			/*out.write("	pointcut pcut():" + "call" + "(" + selectfuction[0] + ")");
			out.write("	around(): pcut(){\n");
			out.write("	" + "long startTime = System.currentTimeMillis()" + "\n}\n");
			out.write("	after(): pcut(){\n");
			out.write("	" + "long endTime = System.currentTimeMillis();" + "System.out.println(\"" + selectfuction[0]
					+ "Time Cost：\" + (endTime - startTime) + \"ms\");" + "\n}\n");
			out.write("}");
			out.close();*/
			MessageBox messageBox = new MessageBox(Editor.shell, SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
			messageBox.setMessage("aj文件生成成功");
			messageBox.open();
		} else {
			File f = new File(pathname + "\\addaj");
			f.mkdir();
			String ajFilename = "\\addaj\\add" + (count++) + ".aj";
			BufferedWriter out = new BufferedWriter(new FileWriter(pathname + ajFilename));
			out.write("public aspect add" + (count - 1) + "{\n");
			out.write("	pointcut pcut():" + Way + "(" + selectfuction[0] + ")");
			for (int i = 1; i < selectfuction.length; i++) {
				out.write("|| " + Way + "(" + selectfuction[i] + ")");
			}
			out.write(";\n");
			if (Active.equals("after( Formals )"))
				out.write("	after(): pcut(){\n");
			if (Active.equals("before( Formals )"))
				out.write("	before(): pcut(){\n");
			if (Active.equals("after( Formals ) returning [ ( Formal ) ]"))
				out.write("	after() returning (Object o): pcut(){\n");
			if (Active.equals("after( Formals ) throwing [ ( Formal ) ]"))
				out.write("	after() throwing (Exception e): pcut(){\n");
			out.write("	" + content + "\n}\n");
			out.write("}");
			out.close();
			MessageBox messageBox = new MessageBox(Editor.shell, SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
			messageBox.setMessage("aj文件生成成功");
			messageBox.open();
		}
	}

	public static int getUndoCount() {
		return undoCount;
	}

	public static void setUndoCount(int undoCount) {
		addcode.undoCount = undoCount;
	}
}
