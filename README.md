# OpenTrax

![GitHub Workflow Status](https://img.shields.io/github/workflow/status/1995parham/OpenTrax/build?label=build&logo=github&style=flat-square)

## Introduction

[Trax](http://www.traxgame.com/) game implementation in Java and Swing based on [GnuTrax](https://github.com/MartinMSPedersen/GnuTrax)
from Martin Møller Skarbiniks Pedersen.
It was written at 2015 for AUT FPGA Competition which was based on Trax Game and this acted as GUI and Judge for the game.

## How to Run

```sh
gradle run
```

## How to Test?

These are the test cases that we validated before competition:

1. Check wrong move from FPGA:

```
  @
 +---+
0| + |   --->   No Neighbours
 +---+   @0+
```

2. Invalid turn move
