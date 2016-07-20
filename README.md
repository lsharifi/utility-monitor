# utility-monitor

We have developed a monitoring infrastructure to collect several system-related metrics during the execution of the workloads for measurements. Figure 1 (available in the repo) presents the elements that constitute this system. The main monitoring component is located at the host, where one or more guest VMs are running. This component  collects four types of data:
i) Frequency at which each of the cores is running;
ii) Performance counters, including the number of processing cycles, the number of instructions, the references and misses when accessing the cache and page faults of the virtual memory;
iii) Overall CPU usage, memory usage, disk reads and writes;
iv) Instantaneous power consumption by reading an external power meter attached to the host.

Guests are responsible for the life cycle of monitoring activities. When a new experiment starts at the guests, a notification is sent to the host, requesting a specific type of monitor to start. The guest also notifies the host to stop a given monitor when the experiment finishes. The host collects the requested information and redirects it to a logger machine, to minimize interference with disk operations. Communication between all parties is socket-based.
