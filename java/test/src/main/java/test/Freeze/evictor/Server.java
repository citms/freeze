// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// **********************************************************************

package test.Freeze.evictor;
import test.Freeze.evictor.Test.*;

public class Server extends test.Util.Application
{
    static class AccountFactory implements Ice.ValueFactory
    {
        public Ice.Object
        create(String type)
        {
            assert(type.equals("::Test::Account"));
            return new AccountI();
        }
    }

    static class ServantFactory implements Ice.ValueFactory
    {
        public Ice.Object
        create(String type)
        {
            assert(type.equals("::Test::Servant"));
            _ServantTie tie = new _ServantTie();
            tie.ice_delegate(new ServantI(tie));
            return tie;
        }
    }

    static class FacetFactory implements Ice.ValueFactory
    {
        public Ice.Object
        create(String type)
        {
            assert(type.equals("::Test::Facet"));
            _FacetTie tie = new _FacetTie();
            tie.ice_delegate(new FacetI(tie));
            return tie;
        }
    }

    public int
    run(String[] args)
    {
        Ice.Communicator communicator = communicator();

        Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Evictor");

        communicator.getValueFactoryManager().add(new AccountFactory(), "::Test::Account");
        communicator.getValueFactoryManager().add(new ServantFactory(), "::Test::Servant");
        communicator.getValueFactoryManager().add(new FacetFactory(), "::Test::Facet");

        RemoteEvictorFactoryI factory = new RemoteEvictorFactoryI("db");
        adapter.add(factory, Ice.Util.stringToIdentity("factory"));

        adapter.activate();

        communicator.waitForShutdown();

        return 0;
    }

    protected Ice.InitializationData
    getInitData(Ice.StringSeqHolder argsH)
    {
        Ice.InitializationData initData = createInitializationData() ;
        initData.properties = Ice.Util.createProperties(argsH);
        initData.properties.setProperty("Evictor.Endpoints", "default -p 12010");
        initData.properties.setProperty("Ice.Package.Test", "test.Freeze.evictor");
        return initData;
    }

    public static void
    main(String[] args)
    {
        Server c = new Server();
        int status = c.main("Server", args);

        System.gc();
        System.exit(status);
    }
}
