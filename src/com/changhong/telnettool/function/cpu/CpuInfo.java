package com.changhong.telnettool.function.cpu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class CpuInfo {
    /*
        system type             : RTL8197F
        machine                 : Unknown
        processor               : 0
        cpu model               : MIPS 24Kc V8.5
        BogoMIPS                : 666.41
        wait instruction        : yes
        microsecond timers      : yes
        tlb_entries             : 64
        extra interrupt vector  : yes
        hardware watchpoint     : yes, count: 4, address/irw mask: [0x0000, 0x0ff8, 0x0ff8, 0x0ff8]
        isa                     : mips1 mips2 mips32r2
        ASEs implemented        : mips16
        shadow register sets    : 4
        kscratch registers      : 0
        core                    : 1
        VCED exceptions         : not available
        VCEI exceptions         : not available
    */
    private String system_type;
    private int core;
    private String cpu_model;
    private String bogoMIPS;
    private boolean wait_instruction;
    private boolean microsecond_timers;
    private int tlb_entries;
    private String isa;
    private String ASEs_implemented;
    private int shadow_register_sets;

    /**
     * 解析 linux 命令 cat /prop/cpuinfo得到的数据
     *
     * @param cpu linux 命令 cat /prop/cpuinfo得到的结果
     */
    public CpuInfo(String cpu) {
        BufferedReader bufferedReader = new BufferedReader(new StringReader(cpu));
        try {
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                int index_colon = line.indexOf(':');
                if (index_colon == -1)
                    continue;


                String key = line.substring(0, index_colon).trim();
                String value = line.substring(index_colon + 1).trim();

                if (key.equals("system type"))
                    system_type = value;
                else if (key.equals("core"))
                    core = Integer.parseInt(value);
                else if (key.equals("tlb_entries"))
                    tlb_entries = Integer.parseInt(value);
                else if (key.equals("cpu model"))
                    cpu_model = value;
                else if (key.equals("BogoMIPS"))
                    bogoMIPS = value;
                else if (key.equals("ASEs implemented"))
                    ASEs_implemented = value;
                else if (key.equals("shadow register sets"))
                    shadow_register_sets = Integer.parseInt(value);
                else if (key.equals("wait instruction"))
                    wait_instruction = Boolean.parseBoolean(value);
                else if (key.equals("microsecond timers"))
                    microsecond_timers = Boolean.parseBoolean(value);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSystem_type() {
        return system_type;
    }

    public int getCore() {
        return core;
    }

    public String getCpu_model() {
        return cpu_model;
    }

    public String getBogoMIPS() {
        return bogoMIPS;
    }

    public boolean isWait_instruction() {
        return wait_instruction;
    }

    public boolean isMicrosecond_timers() {
        return microsecond_timers;
    }

    public int getTlb_entries() {
        return tlb_entries;
    }

    public String getIsa() {
        return isa;
    }

    public String getASEs_implemented() {
        return ASEs_implemented;
    }

    public int getShadow_register_sets() {
        return shadow_register_sets;
    }

    @Override
    public String toString() {
        return "CpuInfo{" +
                "system_type='" + system_type + '\'' +
                ", core=" + core +
                ", cpu_model='" + cpu_model + '\'' +
                ", bogoMIPS='" + bogoMIPS + '\'' +
                ", wait_instruction=" + wait_instruction +
                ", microsecond_timers=" + microsecond_timers +
                ", tlb_entries=" + tlb_entries +
                ", isa='" + isa + '\'' +
                ", ASEs_implemented='" + ASEs_implemented + '\'' +
                ", shadow_register_sets=" + shadow_register_sets +
                '}';
    }
}
