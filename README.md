# utility-monitor

We have developed a monitoring infrastructure to collect several system-related metrics during the execution of the workloads for measurements. Figure \ref{fig:monitor} presents the elements that constitute this system. The main monitoring component is located at the host, where one or more guest VMs are running. This component  collects four types of data:
\begin{inparaenum}[i)]
\item Frequency at which each of the cores is running;
\item Performance counters, including the number of processing cycles, the number of instructions, the references and misses when accessing the cache and page faults of the virtual memory;
\item Overall CPU usage, memory usage, disk reads and writes;
\item Instantaneous power consumption by reading an external power meter attached to the host.
\end{frame}

\begin{figure}
\centering
\includegraphics[scale = 0.40]{./Figs/monitor-tcc.png}
\caption{Monitoring infrastructure}
\label{fig:monitor}
\end{figure}

Guests are responsible for the life cycle of monitoring activities. 
When a new experiment starts at the guests, a notification is sent to the host, requesting a specific type of monitor to start. The guest also notifies the host to stop a given monitor when the experiment finishes. The host collects the requested information and redirects it to a \textit{logger} machine, to minimize interference with disk operations. Communication between all parties is socket-based.
