#!/usr/bin/env node
'use strict';

const fs = require('fs');
const path = require('path');

const JSON_PATH = '/Users/conorheffron/workspace/ironoc/postman/collections/277886-9a656704-20fc-42da-bf5b-e6fc38843f3e.json';
const YAML_DIR = '/Users/conorheffron/workspace/ironoc/postman/postman/collections/iRonoc API';

const collection = JSON.parse(fs.readFileSync(JSON_PATH, 'utf8'));
const graphqlItems = collection.item.filter(item =>
  item.request && item.request.body && item.request.body.mode === 'graphql' &&
  item.name.toLowerCase().startsWith('mutation')
);

// Build exact name map
const byName = {};
for (const item of graphqlItems) {
  byName[item.name] = item;
}

// Explicit mapping: YAML filename stem -> JSON item name
const mapping = [
  ['MUTATION addCharityOption ALONE with Vars', 'MUTATION addCharityOption ALONE with Vars'],
  ['MUTATION addCharityOption Barnardos Ireland with Vars', 'MUTATION addCharityOption Barnardos Ireland with Vars'],
  ['MUTATION addCharityOption CHI at Blanchardstown with Vars', 'MUTATION addCharityOption CHI at Blanchardstown with Vars'],
  ['MUTATION addCharityOption CHI at Connolly with Vars', 'MUTATION addCharityOption CHI at Connolly with Vars'],
  ['MUTATION addCharityOption CHI at Crumlin with Vars', 'MUTATION addCharityOption CHI at Crumlin with Vars'],
  ["MUTATION addCharityOption CHI at St James's with Vars", "MUTATION addCharityOption CHI at St James's with Vars"],
  ['MUTATION addCharityOption CHI at Tallaght with Vars', 'MUTATION addCharityOption CHI at Tallaght with Vars'],
  ['MUTATION addCharityOption CHI at Temple Street with Vars', 'MUTATION addCharityOption CHI at Temple Street with Vars'],
  ["MUTATION addCharityOption Dog's Trust with Vars", "MUTATION addCharityOption Dog's Trust with Vars"],
  ['MUTATION addCharityOption Fealicain with Vars', 'MUTATION addCharityOption Fealicain with Vars'],
  ['MUTATION addCharityOption Make-A-Wish Ireland with Vars', 'MUTATION addCharityOption Make-A-Wish Ireland with Vars'],
  ['MUTATION addCharityOption Marie Keating Foundation with Vars', 'MUTATION addCharityOption Marie Keating Foundation with Vars'],
  ['MUTATION addCharityOption Oesophageal Cancer Fund with Vars', 'MUTATION addCharityOption Oesophageal Cancer Fund with Vars'],
  ['MUTATION addCharityOption Ronald McDonald House Charities with V', 'MUTATION addCharityOption Ronald McDonald House Charities with Vars'],
  ['MUTATION addCharityOption Sightsavers with Vars', 'MUTATION addCharityOption Sightsavers with Vars'],
  ['MUTATION addCharityOption The Irish Heart Foundation with Vars', 'MUTATION addCharityOption The Irish Heart Foundation with Vars'],
  ["MUTATION addCharityOption The New Children's Hospital project wi", "MUTATION addCharityOption The New Children's Hospital project with Vars"],
  ["MUTATION addCharityOption Trócaire with Vars", "MUTATION addCharityOption Trócaire with Vars"],
  ['MUTATION addCharityOption with Vars', 'MUTATION addCharityOption with Vars'],
];

function unescapeJsonString(s) {
  try {
    return JSON.parse('"' + s + '"');
  } catch(e) {
    return s.replace(/\\n/g, '\n').replace(/\\"/g, '"').replace(/\\\\/g, '\\').replace(/\\t/g, '\t');
  }
}

function indentBlock(text, spaces) {
  const indent = ' '.repeat(spaces);
  return text.split('\n').map(line => indent + line).join('\n');
}

function buildYaml(name, query, variables, order) {
  const queryIndented = indentBlock(query, 6);
  let yaml = '$kind: http-request\nname: ' + name + '\nurl: \'{{base_url}}graphql\'\nmethod: POST\nbody:\n  type: graphql\n  content:\n    query: |-\n' + queryIndented + '\n';
  if (variables && variables.trim() !== '') {
    const varsIndented = indentBlock(variables, 6);
    yaml += '    variables: |-\n' + varsIndented + '\n';
  }
  yaml += 'settings: {}\norder: ' + order + '\n';
  return yaml;
}

let fixed = 0;
for (const [stem, jsonName] of mapping) {
  const fname = stem + '.request.yaml';
  const fpath = path.join(YAML_DIR, fname);
  
  if (!fs.existsSync(fpath)) {
    console.log('FILE NOT FOUND:', fname);
    continue;
  }
  
  const content = fs.readFileSync(fpath, 'utf8');
  const orderMatch = content.match(/^order:\s*(\d+)/m);
  const order = orderMatch ? parseInt(orderMatch[1], 10) : 1000;
  
  if (!byName[jsonName]) {
    console.log('JSON ITEM NOT FOUND:', jsonName);
    continue;
  }
  
  const item = byName[jsonName];
  const query = unescapeJsonString(item.request.body.graphql.query);
  const variables = item.request.body.graphql.variables ? unescapeJsonString(item.request.body.graphql.variables) : '';
  
  const newYaml = buildYaml(stem, query, variables, order);
  fs.writeFileSync(fpath, newYaml, 'utf8');
  console.log('Fixed:', fname);
  fixed++;
}
console.log('Total fixed:', fixed);
