# HOWWWWW
name='name';signature='signature';value='value'
d = eval (input ()) # Lazy
def output (single):
    l = len (single)
    for i in range (0, l, 64):
        e = min (l, i + 64)
        print ('                "%s"' % single [i:e], end=',\n' if e == l else '\n')
output (d [name])
output (d [value])
output (d [signature])
