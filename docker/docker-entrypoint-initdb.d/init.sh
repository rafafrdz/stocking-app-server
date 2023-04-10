#!/bin/bash
psql -U admin -d stockingapp -f /raw/data/stockingapp.sql
