# GDB Driver

GDB Driver is a tool to configure gdb debugger (including setting up breakpoints, catchers)
with code of the program, user is debugging, being shown and ability to scroll up and down.

## To Run a project

To run a project simply clone it.

```bash
git clone https://github.com/kornilov-mr/gdbDriver.git
```

## Usage

**Driver:**
The main class of the project, which takes two configs
```java
Driver driver = new Driver(new debuggerConfig(), new OutputConfig());
driver.load(sourceFile);      //cpp or c file, what you want to debug.
driver.run();
```
With empty configs it will just run the code normaly without errorCatcher or any breakpoints

<br/>

**OutputConfig:**

Configures how code and it's veriable will be shown in termenal whenever gdb hits a breakpoint triggers a catchcer

Takes three or fewer parameters (enable/disable showing local variables, range of lines of code, what will be shown, enable/disable console output)
```java
OutputConfig outputConfig = new OutputConfig(true,3,true);
```

Also has setLogFile function to set the output file if needed
```java
outputConfig.setLogFile(logFile); //txt file, where output will go.
```

<br/>

**BreakPoint:**
It requires a file name and line, where should be set
```java
BreakPoint breakPoint = new BreakPoint("example.cpp", 11);
```

<br/>

**Catcher:**
```java
ErrorCatcher catcher = new ErrorCatcher();
```

<br/>

**Callbacks:**
Callbacks are function what are exucuted when gdb hits breakpoint or catches an error, to which callbacks are set

Elementary callbacks (Runnable implementaion)
```java
testBreakPoint.addCallback(() -> {
  System.out.println("Main Thread hit the user's breakpoint");
});
```

Simple callbacks (SimpleCallBackInterface implementation),
Used to use output configurations from Driver
```java
testBreakPoint.addCallback((OutputConfig outputConfig) -> {
  outputConfig.writeLine("Main Thread hit the user's breakpoint");
});
```

Integrated callbacks (IntegratedCallBackInterface implmentaion)
Used to add Commands directly to the command queue. 
```java
breakPoint.addCallback((OutputConfig outputConfig, UserCommandQueue userCommandQueue) -> {
  outputConfig.writeLine("Variable was successfully changed");
  userCommandQueue.addCommand("set variable g=1");
  userCommandQueue.addCommand("continue");
});
```
*Use case: (displaying all local variables when gdb catches an error)*

<br/>

**DebuggerConfig:**

Configures breakpoint and errorCatcher and their callbacks, takes a path to the debugger tool or PATH variable
```java
DebuggerConfig debuggerConfig = new DebuggerConfig("gdb");. // Other degugger than gdb are not implemented
```

Function to add a Breakpoint
```java
debuggerConfig.addBreakPoint(breakPoint);
```

Funciton to set up a Catcher
```java
debuggerConfig.setCatcher(errorCatcher);
```

<br/>

## Use exaples
There are 4 use examples.
They are located in (src/main/java/examples) with cpp source files for them

<br/>

## Code scrolling
3 commands were implemented on top of gdb already existed commands for code scrolling.
Up, Down and Reset (and their variations). 
To go up and down in the source file line.

```bash
 4|                                    ------------------------
 5|    int arr[3];                     |i = 0|
>6|    for(int i=0;i<3;i++){           |arr = {0, 2038120, 0}|
 7|        cin>>arr[i];                ------------------------
 8|    }
```

```bash
 1|#include <iostream>                       ------------------------
 2|using namespace std;                      |i = 0|
 3|int main(){                               |arr = {0, 6757688, 0}|
 4|                                          ------------------------
 5|    int arr[3];
##################################
>6|    for(int i=0;i<3;i++){
```
