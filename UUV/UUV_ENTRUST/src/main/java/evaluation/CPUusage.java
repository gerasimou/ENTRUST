package evaluation;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class CPUusage {

	public static void printUsage() {
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
			method.setAccessible(true);
			if (method.getName().startsWith("get") && Modifier.isPublic(method.getModifiers())) {
				Object value;
				try {
					value = method.invoke(operatingSystemMXBean);
				} catch (Exception e) {
					value = e;
				} // try
				System.out.println(method.getName() + " = " + value);
			} // if
		} // for
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		printUsage();
		measuringCPU();
	}

	
	
	private static void measuringCPU(){
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		long time = threadMXBean.getCurrentThreadCpuTime();
		System.err.println(time);
	}
}
