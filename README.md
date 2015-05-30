softplc
=======

Simulation of a PLC for integration testing of SCADA systems. Originally planned to make a simulation of Siemens Simatic PLC. However over time decided to make a generic PLC. Network protocols are pluggable via classpath and @SoftplcTaskFactory + @SoftplcResponseFactory annotations.

S7 protocol was implemented by my company bus is closed source. I plan to implement some simple protocol like MODBUS TCP when I have some time to get complete open source reference. However concentrating on other features first.

Current functionality:
- multiple configurable CPUs per PLC
- each CPU executes a JavaScript program
- serves pluggable network protocols
- manual access to memory via "variable tables"
- GUI

See wiki for more information and docs: https://github.com/peteral/softplc/wiki

Licence: http://opensource.org/licenses/MIT

Special thanks to:
- https://github.com/ 
- http://sourceforge.net/projects/libnodave/
- https://www.wireshark.org/ 
- http://astah.net/editions/community
