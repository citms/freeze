// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

#pragma once

[["cpp:header-ext:h", "objc:header-dir:objc"]]

/**
 *
 * Freeze provides automatic persistence for Ice servants.
 *
 **/
module Freeze
{

/**
 *
 * A database key, represented as a sequence of bytes.
 *
 **/
sequence<byte> Key;

/**
 *
 * A database value, represented as a sequence of bytes.
 *
 **/
sequence<byte> \Value;

};

