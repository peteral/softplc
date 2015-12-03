softplc
=======

Simulation of a PLC for integration testing of SCADA systems. Originally planned to make a simulation of Siemens Simatic PLC. However over time decided to make a generic PLC. Network protocols are pluggable via @Protocol annotation.

My company runs an internal fork with S7 protocol implementation. Planning to implement at least modbus as reference protocol implementation.

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