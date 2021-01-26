package simplepdl.util;

import java.util.*;
import java.util.Collections;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import petriNet.*;
import simplepdl.*;

public class SimplePDL2PetriNet {
    
    static PetriNet PetriNetwork;
    static PetriNetFactory PetriNetFactory;
    
    static Map<String, Place> resources = new HashMap<String, Place>();
    static Map<String, Place> startedPlaces = new HashMap<String, Place>();
    static Map<String, Place> finishedPlaces = new HashMap<String, Place>();
    static Map<String, Transition> startTransitions = new HashMap<String, Transition>();
    static Map<String, Transition> finishTransitions = new HashMap<String, Transition>();

    
    public static void convertWorkDefinition(WorkDefinition wd)
    {
    	//PLACES
        Place p_notStarted = PetriNetFactory.createPlace();
        p_notStarted.setName(wd.getName() + "_notStarted");
        p_notStarted.setTokensNb(1);
        p_notStarted.setNet(PetriNetwork);
        PetriNetwork.getNodes().add(p_notStarted);
        
        Place p_started = PetriNetFactory.createPlace();
        p_started.setName(wd.getName() + "_started");
        p_started.setTokensNb(0);
        p_started.setNet(PetriNetwork);
        PetriNetwork.getNodes().add(p_started);
        
        Place p_inProgress = PetriNetFactory.createPlace();
        p_inProgress.setName(wd.getName() + "_inProgress");
        p_inProgress.setTokensNb(0);
        p_inProgress.setNet(PetriNetwork);
        PetriNetwork.getNodes().add(p_inProgress);

        Place p_finished = PetriNetFactory.createPlace();
        p_finished.setName(wd.getName() + "_finished");
        p_finished.setTokensNb(0);
        p_finished.setNet(PetriNetwork);
        PetriNetwork.getNodes().add(p_finished);
        
        //TRANSITIONS
        Transition t_start = PetriNetFactory.createTransition();
        t_start.setName(wd.getName() + "_start");
        t_start.setNet(PetriNetwork);
        PetriNetwork.getNodes().add(t_start);
        
        Transition t_finish = PetriNetFactory.createTransition();
        t_finish.setName(wd.getName() + "_finish");
        t_finish.setNet(PetriNetwork);
        PetriNetwork.getNodes().add(t_finish);
        
        //ARCS
        Arc arc_notStarted2start = PetriNetFactory.createArc();
        arc_notStarted2start.setSource(p_notStarted);
        arc_notStarted2start.setTarget(t_start);
        arc_notStarted2start.setTokensNb(1);
        arc_notStarted2start.setNet(PetriNetwork);
        PetriNetwork.getArcs().add(arc_notStarted2start);
        
        Arc arc_start2started = PetriNetFactory.createArc();
        arc_start2started.setSource(t_start);
        arc_start2started.setTarget(p_started);
        arc_start2started.setTokensNb(1);
        arc_start2started.setNet(PetriNetwork);
        PetriNetwork.getArcs().add(arc_start2started);
        
        Arc arc_start2inProgress = PetriNetFactory.createArc();
        arc_start2inProgress.setSource(t_start);
        arc_start2inProgress.setTarget(p_inProgress);
        arc_start2inProgress.setTokensNb(1);
        arc_start2inProgress.setNet(PetriNetwork);
        PetriNetwork.getArcs().add(arc_start2inProgress);
        
        Arc arc_inProgress2finish = PetriNetFactory.createArc();
        arc_inProgress2finish.setSource(p_inProgress);
        arc_inProgress2finish.setTarget(t_finish);
        arc_inProgress2finish.setTokensNb(1);
        arc_inProgress2finish.setNet(PetriNetwork);
        PetriNetwork.getArcs().add(arc_inProgress2finish);
        
        Arc arc_finish2finished = PetriNetFactory.createArc();
        arc_finish2finished.setSource(t_finish);
        arc_finish2finished.setTarget(p_finished);
        arc_finish2finished.setTokensNb(1);
        arc_finish2finished.setNet(PetriNetwork);
        PetriNetwork.getArcs().add(arc_finish2finished);
        
        startedPlaces.put(wd.getName(), p_started);
        finishedPlaces.put(wd.getName(), p_finished);
        startTransitions.put(wd.getName(), t_start);
        finishTransitions.put(wd.getName(), t_finish);
    }
    
    public static void convertWorkDefinitions(simplepdl.Process process)
    {
        for (Object obj : process.getProcessElements())
        {
            if (obj instanceof WorkDefinition)
            {
                convertWorkDefinition((WorkDefinition) obj);
            }
        }
    }
    
    public static void convertWorkSequence(WorkSequence ws)
    {
        Arc arc = PetriNetFactory.createArc();
        
        if (ws.getLinkType() == WorkSequenceType.FINISH_TO_START || ws.getLinkType() == WorkSequenceType.FINISH_TO_FINISH)
        {
            arc.setSource(finishedPlaces.get(ws.getPredecessor().getName()));
        } else {
        	arc.setSource(startedPlaces.get(ws.getPredecessor().getName()));
        }
        
        if(ws.getLinkType() == WorkSequenceType.FINISH_TO_START || ws.getLinkType() == WorkSequenceType.START_TO_START)
        {
            arc.setTarget(startTransitions.get(ws.getSuccessor().getName()));
        }
        else
        {
            arc.setTarget(finishTransitions.get(ws.getSuccessor().getName()));
        }
        arc.setKind(ArcKind.READ_ARC);
        arc.setNet(PetriNetwork);
        arc.setTokensNb(1);
        PetriNetwork.getArcs().add(arc);
    }
    
    public static void convertWorkSequences(simplepdl.Process process)
    {
        for (Object obj : process.getProcessElements())
        {
            if (obj instanceof WorkSequence)
            {
                convertWorkSequence((WorkSequence) obj);
            }
        }
    }
    
    public static void convertAllocation(Allocation allocation)
    {
        Place p_resource = resources.get(allocation.getResources().getName() + "_resource");
        Transition t_start = startTransitions.get(allocation.getWd().getName());
        Transition t_finish = finishTransitions.get(allocation.getWd().getName());
    	
    	Arc arc_allocateResource = PetriNetFactory.createArc();
    	arc_allocateResource.setSource(p_resource);
    	arc_allocateResource.setTarget(t_start);
    	arc_allocateResource.setTokensNb(allocation.getCount());
    	arc_allocateResource.setNet(PetriNetwork);
        PetriNetwork.getArcs().add(arc_allocateResource);
    	
    	Arc arc_deallocateResource = PetriNetFactory.createArc();
    	arc_deallocateResource.setSource(p_resource);
    	arc_deallocateResource.setTarget(t_finish);
    	arc_deallocateResource.setTokensNb(allocation.getCount());
    	arc_deallocateResource.setNet(PetriNetwork);
        PetriNetwork.getArcs().add(arc_deallocateResource);
    }

    public static void convertResource(simplepdl.Resource resource)
    {
        
        Place p_resource = PetriNetFactory.createPlace();
        p_resource.setName(resource.getName() + "_resource");
        p_resource.setTokensNb(resource.getCount());
        p_resource.setNet(PetriNetwork);
        PetriNetwork.getNodes().add(p_resource);

        resources.put(resource.getName() + "_resource", p_resource);
        for (Allocation allocation :  resource.getAllocations())
        {
            convertAllocation(allocation);
        }
    }
    
    public static void convertResources(simplepdl.Process process)
    {
        for (Object obj : process.getProcessElements())
        {
            if (obj instanceof simplepdl.Resource)
            {
                convertResource((simplepdl.Resource) obj);
            }
        }
    }
        
    public static void main(String[] args)
    {

    	String simplePdlModel = args.length >=1 ? args[0] : "developpement";

    	SimplepdlPackage simplePdlPackageInstance = SimplepdlPackage.eINSTANCE;
    	PetriNetPackage petriNetworkPackageInstance = PetriNetPackage.eINSTANCE;
        

        Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        Map<String, Object> extensionToFactoryMap = reg.getExtensionToFactoryMap();
        extensionToFactoryMap.put("xmi", new XMIResourceFactoryImpl());
                
        ResourceSet resSet = new ResourceSetImpl();

        URI modelURI = URI.createURI("models/" + simplePdlModel + ".xmi");
        Resource resource = resSet.getResource(modelURI, true);
        
        simplepdl.Process process = (simplepdl.Process)resource.getContents().get(0);
        
        URI petriNetworkURI = URI.createURI("models/" + simplePdlModel + "2Petri_JAVA.xmi");
        Resource petriNetwork = resSet.createResource(petriNetworkURI);
        
        PetriNetFactory = petriNet.PetriNetFactory.eINSTANCE;
        PetriNetwork = PetriNetFactory.createPetriNet();
        PetriNetwork.setName(process.getName());
        petriNetwork.getContents().add(PetriNetwork);
        
        convertWorkDefinitions(process);
        convertWorkSequences(process);
        convertResources(process);
 
        try {
        	petriNetwork.save(Collections.EMPTY_MAP);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}



