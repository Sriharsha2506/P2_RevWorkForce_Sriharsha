import os, re
controller_dir = r'c:\Users\tharu\IdeaProjects\P2_Rev_Workforce\src\main\java\com\rev\app\controller'

def fix_file(path):
    with open(path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Fix @PathVariable Type name -> @PathVariable("name") Type name
    content = re.sub(r'@PathVariable\s+([\w<>]+)\s+(\w+)', r'@PathVariable("\2") \1 \2', content)

    # Fix @RequestParam Type name -> @RequestParam("name") Type name
    content = re.sub(r'@RequestParam\s+([\w<>]+)\s+(\w+)', r'@RequestParam("\2") \1 \2', content)

    # Fix @RequestParam(something) Type name -> check if name/value exists, if not add it
    def req_repl(m):
        ann_args = m.group(1)
        type_str = m.group(2)
        var_name = m.group(3)
        if 'value' not in ann_args and 'name' not in ann_args and '"' not in ann_args.split('=')[0]:
            return f'@RequestParam(value = "{var_name}", {ann_args}) {type_str} {var_name}'
        return m.group(0)
    
    content = re.sub(r'@RequestParam\(([^)]+)\)\s+([\w<>]+)\s+(\w+)', req_repl, content)
    
    # Fix @PathVariable(something) Type name -> check if name/value exists, if not add it
    def path_repl(m):
        ann_args = m.group(1)
        type_str = m.group(2)
        var_name = m.group(3)
        if 'value' not in ann_args and 'name' not in ann_args and '"' not in ann_args.split('=')[0]:
            return f'@PathVariable(value = "{var_name}", {ann_args}) {type_str} {var_name}'
        return m.group(0)
        
    content = re.sub(r'@PathVariable\(([^)]+)\)\s+([\w<>]+)\s+(\w+)', path_repl, content)

    with open(path, 'w', encoding='utf-8') as f:
        f.write(content)

for root, dirs, files in os.walk(controller_dir):
    for fn in files:
        if fn.endswith('.java'):
            fix_file(os.path.join(root, fn))
print('Done!')
