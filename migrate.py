import re

# This file is for migrating the old commands to the new ones

def macro (cmd):
    # item replace entity @p container.4 with pkcp:pkmacro{"macro":"..."} ->
    # item replace entity @p container.4 with pkcp:macro[pkcp:macro={"macro":"..."}]
    return cmd [:52] + '[pkcp:macro=' + cmd [52:] + ']'

def pkcp (cmd):
    nbt = cmd [49:-1]
    nbt = nbt.replace ('rot', 'r')
    nbt = re.sub ('mode:([123])', r'mode:{landmode:\1}', nbt)
    nbt = nbt.replace ('pos:{', '')
    nbt = re.sub (r'block:{x:(\-?[0-9]+),y:(\-?[0-9]+),z:(\-?[0-9]+)}', r'landing:[I;\1,\2,\3]', nbt)
    return cmd [:49] + '[pkcp:pkcp=' + nbt + ']'
