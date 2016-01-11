c = new GameConsole()

def n = { width, height, bombs ->
    c.newGame(width, height, bombs)
    c.status()
}

def o = { x, y ->
    c.open x, y
    c.status()
}

def f = { x, y ->
    c.flag x, y
    c.status()
}

def s = {
    c.status()
}

s()
