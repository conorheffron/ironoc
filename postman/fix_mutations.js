#!/usr/bin/env node
'use strict';

const fs = require('fs');
const path = require('path');

const JSON_PATH = '/Users/conorheffron/workspace/ironoc/postman/collections/277886-9a656704-20fc-42da-bf5b-e6fc38843f3e.json';
const YAML_DIR = '/Users/conorheffron/workspace/ironoc/postman/postman/collections/iRonoc API';
const SKIP_FILE = 'MUTATION addCharityOption My Lovely Horse Animal Rescue with Var.request.yaml';

// Read JSON collection
const collection = JSON.parse(fs.readFileSync(JSON_PATH, 'utf8'));

// Extract all graphql mutation items (exclude GET Donate Items query)
const graphqlItems = collection.item.filter(item =>
  item.request && item.request.body && item.request.body.mode === 'graphql' &&
  item.name.toLowerCase().startsWith('mutation')
);

console.log(`Found ${graphqlItems.length} GraphQL mutation items in JSON collection`);
graphqlItems.forEach(i => console.log('  JSON item:', i.name));

// Read all YAML files in the directory
const yamlFiles = fs.readdirSync(YAML_DIR).filter(f => f.endsWith('.request.yaml'));

// Find files that need fixing (have query: "")
const toFix = [];
for (const fname of yamlFiles) {
  if (fname === SKIP_FILE) {
    console.log(`Skipping already-fixed: ${fname}`);
    continue;
  }
  const fpath = path.join(YAML_DIR, fname);
  const content = fs.readFileSync(fpath, 'utf8');
  if (content.includes('query: ""')) {
    toFix.push({ fname, fpath, content });
  }
}

console.log(`\nFiles to fix: ${toFix.length}`);

// Helper: extract order from existing YAML
function extractOrder(content) {
  const m = content.match(/^order:\s*(\d+)/m);
  return m ? parseInt(m[1], 10) : 1000;
}

// Helper: unescape JSON string (convert \n to newline, \" to ", etc.)
function unescapeJsonString(s) {
  try {
    return JSON.parse('"' + s + '"');
  } catch(e) {
    return s.replace(/\\n/g, '\n').replace(/\\"/g, '"').replace(/\\\\/g, '\\').replace(/\\t/g, '\t');
  }
}

// Helper: indent block scalar content by 6 spaces
function indentBlock(text, spaces) {
  const indent = ' '.repeat(spaces);
  return text.split('\n').map(line => indent + line).join('\n');
}

// Helper: build YAML content
function buildYaml(name, query, variables, order) {
  const queryIndented = indentBlock(query, 6);
  let yaml = `$kind: http-request\nname: ${name}\nurl: '{{base_url}}graphql'\nmethod: POST\nbody:\n  type: graphql\n  content:\n    query: |-\n${queryIndented}\n`;
  if (variables && variables.trim() !== '') {
    const varsIndented = indentBlock(variables, 6);
    yaml += `    variables: |-\n${varsIndented}\n`;
  }
  yaml += `settings: {}\norder: ${order}\n`;
  return yaml;
}

// Build a map from JSON item name -> item for exact matching
const jsonByName = {};
for (const item of graphqlItems) {
  jsonByName[item.name.toLowerCase()] = item;
}

// Match each YAML file to a JSON item
let fixed = 0;
let notFound = [];

for (const { fname, fpath, content } of toFix) {
  // Get stem (remove .request.yaml)
  const stem = fname.replace(/\.request\.yaml$/, '');
  
  const prefix = 'MUTATION addCharityOption ';
  const stemKey = stem.toLowerCase().startsWith(prefix.toLowerCase())
    ? stem.slice(prefix.length).toLowerCase()
    : stem.toLowerCase();

  // Find matching JSON item - use exact match on full name first, then prefix-stripped match
  let matched = null;

  // 1. Exact full name match
  if (jsonByName[stem.toLowerCase()]) {
    matched = jsonByName[stem.toLowerCase()];
  }

  // 2. Exact prefix-stripped match
  if (!matched) {
    for (const item of graphqlItems) {
      const itemKey = item.name.toLowerCase().startsWith(prefix.toLowerCase())
        ? item.name.slice(prefix.length).toLowerCase()
        : item.name.toLowerCase();
      if (itemKey === stemKey) {
        matched = item;
        break;
      }
    }
  }

  // 3. Prefix-stripped includes match (stemKey starts with itemKey)
  if (!matched) {
    for (const item of graphqlItems) {
      const itemKey = item.name.toLowerCase().startsWith(prefix.toLowerCase())
        ? item.name.slice(prefix.length).toLowerCase()
        : item.name.toLowerCase();
      // stemKey starts with itemKey (truncated filename)
      if (stemKey.startsWith(itemKey) || itemKey.startsWith(stemKey)) {
        matched = item;
        break;
      }
    }
  }

  if (!matched) {
    console.warn(`  WARNING: No match found for: ${fname} (key: "${stemKey}")`);
    notFound.push(fname);
    continue;
  }

  const order = extractOrder(content);
  const rawQuery = matched.request.body.graphql.query;
  const rawVars = matched.request.body.graphql.variables;

  const query = unescapeJsonString(rawQuery);
  const variables = rawVars ? unescapeJsonString(rawVars) : '';

  const newYaml = buildYaml(matched.name, query, variables, order);
  fs.writeFileSync(fpath, newYaml, 'utf8');
  console.log(`  Fixed: ${fname} -> matched "${matched.name}"`);
  fixed++;
}

console.log(`\nDone! Fixed ${fixed} files.`);
if (notFound.length > 0) {
  console.log(`Not found matches for: ${notFound.join(', ')}`);
}
