c = new GameConsole()

def newGame = { width, height, bombs ->
    c.newGame(width, height, bombs)
    c.status()
}

def open = { x, y ->
    c.open x, y
    c.status()
}

def flag = { x, y ->
    c.flag x, y
    c.status()
}

def status = {
    c.status()
}

n = newGame
o = open
f = flag
s = status

status()
