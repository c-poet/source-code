package org.omg.PortableServer;


/**
* org/omg/PortableServer/IdUniquenessPolicyOperations.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from e:/jenkins/tmp/workspace/build/src/corba/src/share/classes/org/omg/PortableServer/poa.idl
* Wednesday, January 19, 2022 7:08:15 AM CST
*/


/**
	 * The IdUniquenessPolicy specifies whether the servants 
	 * activated in the created POA must have unique object i
	 * identities. The default is UNIQUE_ID.
	 */
public interface IdUniquenessPolicyOperations  extends org.omg.CORBA.PolicyOperations
{

  /**
	 * specifies the policy value
	 */
  org.omg.PortableServer.IdUniquenessPolicyValue value ();
} // interface IdUniquenessPolicyOperations
