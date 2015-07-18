package com.resourcemonitor.daemon;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

/**
 * example shown in lab
 * taklwu 
 * Nov 22, 2013
 */

public class PerformanceExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Sigar sigar = new Sigar();
        Mem mem = null;
        CpuPerc cpuPerc = null;
        try {
            mem = sigar.getMem();
            cpuPerc = sigar.getCpuPerc();
        } catch (SigarException e) {
            e.printStackTrace();
        }
        
        System.out.println("CPU usage =" + cpuPerc.getSys() * 100);
        System.out.println("Memory usage =" + mem.getUsedPercent());
        
        // for prodIDs
        long procIds[] = {1028, 204, 700};
        double cpu = 0;
        double memforPid = 0;
        for (long pid : procIds) {
            try {
                ProcCpu procCpu = new ProcCpu();
                procCpu.gather(sigar, pid);

                ProcMem procMem = new ProcMem();
                procMem.gather(sigar, pid);

                memforPid += procMem.getSize() * 100 / sigar.getMem().getTotal();
                cpu += procCpu.getTotal();
                
            } catch (Exception ignore) {
            }
        }
        System.out.println("CPU usage =" + cpu);
        System.out.println("Memory usage =" + memforPid);
        
	}

}
